package org.mxhero.engine.fsqueues.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.RulePhase;
import org.mxhero.engine.domain.queue.MimeMailQueueService;
import org.mxhero.engine.fsqueues.internal.check.AbandonedCheck;
import org.mxhero.engine.fsqueues.internal.entity.DelayedMail;
import org.mxhero.engine.fsqueues.internal.entity.FSMail;
import org.mxhero.engine.fsqueues.internal.entity.FSMailKey;
import org.mxhero.engine.fsqueues.internal.util.Files;
import org.mxhero.engine.fsqueues.internal.util.SharedTmpFileInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FSQueueService implements MimeMailQueueService {

	public static final String SENDER_HEADER = "X-mxHero-Sender";
	public static final String RECIPIENT_HEADER = "X-mxHero-Recipient";
	public static final String OUTPUT_SERVICE_HEADER = "X-mxHero-Output-Service";
	
	private static Logger log = LoggerFactory.getLogger(FSQueueService.class);
	
	private static Map<FSMailKey,FSMail> store = new Hashtable<FSMailKey,FSMail>();
	
	private static Map<String, DelayQueue<DelayedMail>> queues = new Hashtable<String, DelayQueue<DelayedMail>>();
	
	private FSConfig config;
	
	private AbandonedCheck check;
	
	public FSQueueService(FSConfig config){
		this.config = config;
		check = new AbandonedCheck(store, this, queues);
	}
	
	public void stop(){
		check.stop();
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
			if(!storeFile.isDirectory()){
				MimeMessage data = null;
				String recipient = null;
				String sender = null;
				String outputService = null;
				FSMail fsmail = null;
				MimeMail mail = null;
				File tmpFile = null;
				InputStream is = null;
				OutputStream os = null;
				try{
					data=new MimeMessage(Session.getDefaultInstance(new Properties()), new FileInputStream(storeFile));
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
						os.flush();
						is = new ByteArrayInputStream(((ByteArrayOutputStream)os).toByteArray());
					//if tmp should be on disk
					}else{
						tmpFile = File.createTempFile(config.getTmpPrefix(), config.getSuffix(), config.getTmpPath());
						os = new FileOutputStream(tmpFile);
						data.writeTo(os);
						os.flush();
						is = new SharedTmpFileInputStream(tmpFile);
					}
	
					mail= new MimeMail(sender, recipient, is, outputService);
					fsmail = new FSMail(new FSMailKey(mail.getSequence(), mail.getTime()));
					fsmail.setFile(storeFile.getAbsolutePath());
					if(tmpFile!=null){
						fsmail.setTmpFile(tmpFile.getAbsolutePath());
					}
					getOrCreateQueue(RulePhase.SEND).put(new DelayedMail(mail));
					store.put(fsmail.getKey(), fsmail);
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
		check.init();
	}
	
	public boolean store(String phase, MimeMail mail, long timeout, TimeUnit unit)
    throws InterruptedException{
		File storeFile=null;
		File tmpFile=null;
		FileOutputStream sfos = null;
		FileOutputStream tfos = null;
		InputStream is = null;
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
				sfos.flush();
				mail.getMessage().removeHeader(SENDER_HEADER);
				mail.getMessage().removeHeader(RECIPIENT_HEADER);
				mail.getMessage().removeHeader(OUTPUT_SERVICE_HEADER);
				mail.getMessage().saveChanges();
				
				if(mail.getInitialSize()>config.getDeferredSize()){
					tmpFile = File.createTempFile(config.getTmpPrefix(), config.getSuffix(), config.getTmpPath());
					tfos = new FileOutputStream(tmpFile);
					mail.getMessage().writeTo(tfos);
					tfos.flush();
					fsmail.setTmpFile(tmpFile.getAbsolutePath());
					is = new SharedTmpFileInputStream(tmpFile);
					MimeMail newMail = MimeMail.createCustom(mail.getInitialSender()
							, mail.getRecipient(), 
							is, 
							mail.getResponseServiceId(), 
							mail.getSequence(), 
							mail.getTime());
					newMail.setProperties(mail.getProperties());
					newMail.setPhase(mail.getPhase());
					mail=newMail;
				}else{
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					mail.getMessage().writeTo(os);
					os.flush();
					is = new ByteArrayInputStream(os.toByteArray());
					MimeMail newMail = MimeMail.createCustom(mail.getInitialSender()
							, mail.getRecipient(), 
							is, 
							mail.getResponseServiceId(), 
							mail.getSequence(), 
							mail.getTime());
					newMail.setProperties(mail.getProperties());
					newMail.setPhase(mail.getPhase());
					mail=newMail;
				}
				synchronized (queues) {
					store.put(fsmail.getKey(), fsmail);
					addedToQueue = getOrCreateQueue(phase).offer(new DelayedMail(mail), timeout, unit);
					if(!addedToQueue){
						store.remove(fsmail.getKey());
					}
				}
				return addedToQueue;
			} catch (Exception e) {
				log.error(mail.toString(),e);
			} finally{
				if(sfos!=null){
					try{sfos.close();}catch(Exception e){}
				}
				if(tfos!=null){
					try{tfos.close();}catch(Exception e){}
				}
				if(is!=null){
					try{is.close();}catch(Exception e){}
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
		unstore(mail.getTime(),mail.getSequence());
	}
	
	private void unstore(Timestamp time, Long sequence){
		FSMailKey fsmailKey = new FSMailKey(sequence,time);
		FSMail fsmail = store.remove(fsmailKey);
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
	}
	

	public void saveToAndUnstore(Timestamp time, Long sequence, String path){
		FSMailKey fsmailKey = new FSMailKey(sequence,time);
		FSMail fsmail = store.get(fsmailKey);
		if(fsmail!=null){
			File storeFile=null;
			storeFile = new File(fsmail.getFile());
			File pathTo = new File(path);
			File storeFileTo = new File(pathTo,storeFile.getName());
			try {
				Files.copy(storeFile, storeFileTo);
				unstore(time,sequence);
			} catch (IOException e) {
				log.error("error while saving and unstoring email",e);
			}
		}else{
			log.warn("fail to find mail for key "+fsmailKey);
		}
	}
	
	public void saveToAndUnstore(MimeMail mail, String path, boolean useOriginal){
		FSMailKey fsmailKey = new FSMailKey(mail.getSequence(),mail.getTime());
		FSMail fsmail = store.get(fsmailKey);
		if(fsmail!=null){
			try{
				if(useOriginal){
					File storeFile=null;
					storeFile = new File(fsmail.getFile());
					File pathTo = new File(path);
					File storeFileTo = new File(pathTo,storeFile.getName());
					Files.copy(storeFile, storeFileTo);
				}else{
					mail.getMessage().removeHeader(SENDER_HEADER);
					mail.getMessage().removeHeader(RECIPIENT_HEADER);
					mail.getMessage().removeHeader(OUTPUT_SERVICE_HEADER);
					mail.getMessage().addHeader(SENDER_HEADER, mail.getInitialSender());
					mail.getMessage().addHeader(RECIPIENT_HEADER, mail.getRecipient());
					mail.getMessage().addHeader(OUTPUT_SERVICE_HEADER, mail.getResponseServiceId());
					mail.getMessage().saveChanges();
					File tmpFile = File.createTempFile(config.getTmpPrefix(), config.getSuffix(), config.getTmpPath());
					FileOutputStream tfos = null;
					try{
						tfos = new FileOutputStream(tmpFile);
						mail.getMessage().writeTo(tfos);
					}finally{
						if(tfos!=null){
							tfos.close();
						}
					}
				}
				unstore(mail);
			}catch(Exception e){
				log.error("error while saving and unstoring email",e);
			}
		}
	}
	
	public boolean offer(String phase, MimeMail mail, long timeout, TimeUnit unit)
    throws InterruptedException{
		FSMailKey key = new FSMailKey(mail.getSequence(),mail.getTime());
		if(store.containsKey(key)){
			return getOrCreateQueue(phase).offer(new DelayedMail(mail), timeout, unit);
		}else{
			log.warn("fail to find mail "+mail+" for key "+key);
		}
		return false;
	}
	
	public void put(String phase, MimeMail mail)
    throws InterruptedException{
		FSMailKey key = new FSMailKey(mail.getSequence(),mail.getTime());
		if(store.containsKey(key)){
			getOrCreateQueue(phase).put(new DelayedMail(mail));
		}else{
			log.warn("fail to find mail "+mail+" for key "+key);
		}
	}
	

	public void delayAndPut(String phase, MimeMail mail, long millisenconds)
			throws InterruptedException {
		FSMailKey key = new FSMailKey(mail.getSequence(),mail.getTime());
		if(store.containsKey(key)){
			getOrCreateQueue(phase).put(new DelayedMail(mail,millisenconds));
		}else{
			log.warn("fail to find mail "+mail+" for key "+key);
		}
	}
	
	public MimeMail poll(String phase, long timeout, TimeUnit unit) throws InterruptedException{
		DelayedMail dmail = getOrCreateQueue(phase).poll(timeout, unit);
		if(dmail!=null){
			return dmail.getMail();
		}
		return null;
	}

	public void logState(){
		log.info(getQueuesCount());
	}
	
	private String getQueuesCount() {
		StringBuilder sb = new StringBuilder();
		sb.append("[store="+store.size());
		for(Entry<String, DelayQueue<DelayedMail>> entry : queues.entrySet()){
			sb.append(", "+entry.getKey()+"="+entry.getValue().size());
			}
		sb.append("]");
		return sb.toString();
	}
	
	private DelayQueue<DelayedMail> getOrCreateQueue(String phase){
		DelayQueue<DelayedMail> queue = null;
		synchronized (queues) {
			queue = queues.get(phase);
			if(queue==null){
				queue = new DelayQueue<DelayedMail>();
				queues.put(phase, queue);
			}
		}
		return queue;
	}
	
	
	public int size(){
		return store.size();
	}
	
	public FSConfig getConfig(){
		return this.config;
	}

}
