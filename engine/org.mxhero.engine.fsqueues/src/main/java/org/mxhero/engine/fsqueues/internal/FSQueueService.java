package org.mxhero.engine.fsqueues.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.RulePhase;
import org.mxhero.engine.domain.queue.MimeMailQueueService;
import org.mxhero.engine.fsqueues.internal.entity.FSMail;
import org.mxhero.engine.fsqueues.internal.entity.FSMailKey;
import org.mxhero.engine.fsqueues.internal.util.SharedTmpFileInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FSQueueService implements MimeMailQueueService {

	public static final String SENDER_HEADER = "X-mxHero-Sender";
	public static final String RECIPIENT_HEADER = "X-mxHero-Recipient";
	public static final String OUTPUT_SERVICE_HEADER = "X-mxHero-Output-Service";
	
	private static Logger log = LoggerFactory.getLogger(FSQueueService.class);
	
	private static Map<FSMailKey,FSMail> store = new ConcurrentHashMap<FSMailKey,FSMail>();
	
	private static Map<String, ArrayBlockingQueue<MimeMail>> queues = new ConcurrentHashMap<String, ArrayBlockingQueue<MimeMail>>();
	
	private FSConfig config;
	
	public FSQueueService(FSConfig config){
		this.config = config;
	}
	
	public void stop(){
		queues.clear();
		store.clear();
		System.gc();
	}
	
	public void init(){
		for(File tmpFile : config.getTmpPath().listFiles()){
			tmpFile.delete();
		}
		File[] storeMails = config.getStorePath().listFiles();
		if(storeMails.length>config.getCapacity()){
			config.setCapacity(storeMails.length);
		}
		for(File storeFile : storeMails){
			MimeMessage data = null;
			String recipient = null;
			String sender = null;
			String outputService = null;
			FSMail fsmail = null;
			MimeMail mail = null;
			File tmpFile = null;
			InputStream is = null;
			OutputStream os = null;
			boolean addedToQueue = false;
			try{
				data=new MimeMessage(Session.getDefaultInstance(new Properties()), new SharedTmpFileInputStream(storeFile));
				sender=data.getHeader(SENDER_HEADER)[0];
				recipient=data.getHeader(RECIPIENT_HEADER)[0];
				outputService=data.getHeader(OUTPUT_SERVICE_HEADER)[0];
				data.removeHeader(SENDER_HEADER);
				data.removeHeader(RECIPIENT_HEADER);
				data.removeHeader(OUTPUT_SERVICE_HEADER);
				data.saveChanges();
				
				//if tmp should be in memory
				if(storeFile.length()<config.getDeferredSize()){
					os = new ByteArrayOutputStream();
					data.writeTo(os);
					is = new ByteArrayInputStream(((ByteArrayOutputStream)os).toByteArray());
				//if tmp should be on disk
				}else{
					tmpFile = File.createTempFile(config.getTmpPrefix(), config.getSuffix(), config.getTmpPath());
					os = new FileOutputStream(tmpFile);
					data.writeTo(os);
					is = new SharedTmpFileInputStream(tmpFile);
				}

				mail= new MimeMail(sender, recipient, is, outputService);
				fsmail = new FSMail(new FSMailKey(mail.getSequence(), mail.getTime()));
				fsmail.setFile(storeFile.getAbsolutePath());
				if(tmpFile!=null){
					fsmail.setTmpFile(tmpFile.getAbsolutePath());
				}
				addedToQueue = getOrCreateQueue(RulePhase.SEND).offer(mail);
				if(addedToQueue){
					store.put(fsmail.getKey(), fsmail);
				}
			}catch(Exception e){
				log.error("error loading email "+storeFile.getAbsolutePath(),e);
				continue;
			}finally{
				if(os!=null){
					try{os.close();}catch(Exception e){}
				}
				if(is!=null){
					try{is.close();}catch(Exception e){}
				}
			}
		}
	}
	
	public boolean store(String phase, MimeMail mail, long timeout, TimeUnit unit)
    throws InterruptedException{
		File storeFile=null;
		File tmpFile=null;
		FileOutputStream sfos = null;
		FileOutputStream tfos = null;
		FSMail fsmail = null;
		boolean addedToQueue = false;
		//this is not a strict condition at all
		if(store.size()<config.getCapacity()){
			try {
				
				mail.getMessage().removeHeader(SENDER_HEADER);
				mail.getMessage().removeHeader(RECIPIENT_HEADER);
				mail.getMessage().removeHeader(OUTPUT_SERVICE_HEADER);
				mail.getMessage().addHeader(SENDER_HEADER, mail.getInitialSender());
				mail.getMessage().addHeader(RECIPIENT_HEADER, mail.getRecipient());
				mail.getMessage().addHeader(OUTPUT_SERVICE_HEADER, mail.getResponseServiceId());
				mail.getMessage().saveChanges();
				
				fsmail = new FSMail(new FSMailKey(mail.getSequence(),mail.getTime()));
				storeFile = File.createTempFile(config.getStorePrefix(), config.getSuffix(), config.getStorePath());
				sfos = new FileOutputStream(storeFile);
				fsmail.setFile(storeFile.getAbsolutePath());
				mail.getMessage().writeTo(sfos);
				
				mail.getMessage().removeHeader(SENDER_HEADER);
				mail.getMessage().removeHeader(RECIPIENT_HEADER);
				mail.getMessage().removeHeader(OUTPUT_SERVICE_HEADER);
				mail.getMessage().saveChanges();
				
				if(mail.getInitialSize()>config.getDeferredSize()){
					tmpFile = File.createTempFile(config.getTmpPrefix(), config.getSuffix(), config.getTmpPath());
					tfos = new FileOutputStream(tmpFile);
					mail.getMessage().writeTo(tfos);
					fsmail.setTmpFile(tmpFile.getAbsolutePath());
					MimeMail newMail = MimeMail.createCustom(mail.getInitialSender()
							, mail.getRecipient(), 
							new SharedTmpFileInputStream(tmpFile), 
							mail.getResponseServiceId(), 
							mail.getSequence(), 
							mail.getTime());
					newMail.setProperties(mail.getProperties());
					newMail.setPhase(mail.getPhase());
					mail=newMail;
				}
				addedToQueue = getOrCreateQueue(phase).offer(mail, timeout, unit);
				if(addedToQueue){
					store.put(fsmail.getKey(), fsmail);
				}
				log.debug("added "+mail.toString());
				return true;
			} catch (Exception e) {
				log.error(mail.toString(),e);
				return false;
			} finally{
				if(sfos!=null){
					try{sfos.close();}catch(Exception e){}
				}
				if(tfos!=null){
					try{tfos.close();}catch(Exception e){}
				}
				//if email was not added, them clean things up
				if(addedToQueue==false){
					if(fsmail!=null){
						store.remove(fsmail.getKey());
					}
					if(storeFile!=null){
						try{storeFile.delete();}catch(Exception e){}
					}
					if(tmpFile!=null){
						try{tmpFile.delete();}catch(Exception e){}
					}
				}
			}
		}
		return false;
	}
	
	public void unstore(MimeMail mail){
		FSMailKey fsmailKey = new FSMailKey(mail.getSequence(),mail.getTime());
		FSMail fsmail = store.get(fsmailKey);
		if(fsmail!=null){
			if(fsmail.getTmpFile()!=null){
				try{
					File tmp = new File(fsmail.getTmpFile());
					if(tmp.exists()){
						tmp.delete();
					}
				}catch(Exception e){
					log.warn(fsmail.getTmpFile(),e);
				}
			}
			if(fsmail.getFile()!=null){
				try{
					File file = new File(fsmail.getFile());
					if(file.exists()){
						file.delete();
					}
				}catch(Exception e){
					log.warn(fsmail.getFile(),e);
				}
			}
		}
		fsmail=store.remove(fsmailKey);
	}
	
	public boolean offer(String phase, MimeMail mail, long timeout, TimeUnit unit)
    throws InterruptedException{
		if(store.containsKey(new FSMailKey(mail.getSequence(),mail.getTime()))){
			return getOrCreateQueue(phase).offer(mail, timeout, unit);
		}
		return false;
	}
	
	public MimeMail poll(String phase, long timeout, TimeUnit unit) throws InterruptedException{
		return getOrCreateQueue(phase).poll(timeout, unit);
	}

	public void logState(){
		log.info(getQueuesCount());
	}
	
	private String getQueuesCount() {
		StringBuilder sb = new StringBuilder();
		sb.append("[store="+store.size());
		for(Entry<String, ArrayBlockingQueue<MimeMail>> entry : queues.entrySet()){
			sb.append(", "+entry.getKey()+"="+entry.getValue().size());
			}
		sb.append("]");
		return sb.toString();
	}
	
	private ArrayBlockingQueue<MimeMail> getOrCreateQueue(String phase){
		ArrayBlockingQueue<MimeMail> queue = null;
		synchronized (queues) {
			queue = queues.get(phase);
			if(queue==null){
				queue = new ArrayBlockingQueue<MimeMail>(config.getCapacity());
				queues.put(phase, queue);
			}
		}
		return queue;
	}
	
	
	public int size(){
		return store.size();
	}
}
