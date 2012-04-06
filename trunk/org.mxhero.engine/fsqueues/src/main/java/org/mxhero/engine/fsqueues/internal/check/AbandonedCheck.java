package org.mxhero.engine.fsqueues.internal.check;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.DelayQueue;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.fsqueues.internal.FSQueueService;
import org.mxhero.engine.fsqueues.internal.entity.DelayedMail;
import org.mxhero.engine.fsqueues.internal.entity.FSMail;
import org.mxhero.engine.fsqueues.internal.entity.FSMailKey;
import org.mxhero.engine.fsqueues.internal.util.SharedTmpFileInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbandonedCheck {

	private static Logger log = LoggerFactory.getLogger(AbandonedCheck.class);
	private Map<FSMailKey,FSMail> store;
	private Map<Mail.Phase, DelayQueue<DelayedMail>> queues;
	private FSQueueService srv;
	private boolean weekWorking=true;
	private long checkTime=180*1000;
	private static final long MAX_TIME_IN_STORE = 20*60*1000;
	private static final int TRIES=3;
	
	public AbandonedCheck(Map<FSMailKey, FSMail> store, FSQueueService srv, Map<Mail.Phase, DelayQueue<DelayedMail>> queues) {
		this.store = store;
		this.srv = srv;
		this.queues = queues;
	}
	
	public void init(){
		new Thread(new Runnable() {
			public void run() {
				while(weekWorking){
					try{
						work();
					}catch(Exception e){
						log.error("error while working",e);
					}
					try {
						Thread.sleep(checkTime);
					} catch (InterruptedException e) {
						log.info("interrupted while working "+e.toString());
					}
				}			
			}
		}).start();
	}
	
	public void stop(){
		weekWorking=false;
	}
	
	public boolean exists(FSMail fsMail){
		DelayedMail mail = new DelayedMail(fsMail.getKey().getTime(), fsMail.getKey().getSequence());
		for(Mail.Phase phase : queues.keySet()){
			if(queues.get(phase).contains(mail)){
				log.warn("mail delayed in phase:"+phase);
				return true;
			}
		}
		return false;
	}
	
	private void work(){
		Collection<FSMail> fsmails = null;
		MimeMail mail = null;
		if(store!=null){
			fsmails = new ArrayList<FSMail>(store.values());
			for (FSMail fsMail : fsmails){
				OutputStream os = null;
				InputStream is = null;
				File tmpFile = null;
				//if last check plus max perios is less than current time
				log.debug("checking "+fsMail);
				if((fsMail.getLastCheck()+MAX_TIME_IN_STORE)<System.currentTimeMillis() && !exists(fsMail)){
					//if we should try again
					if(fsMail.getReadded()<TRIES){
						try{
							fsMail.setReadded(fsMail.getReadded()+1);
							fsMail.setLastCheck(System.currentTimeMillis());
							log.debug("abandoned email "+fsMail);
							File storeFile = new File(fsMail.getFile());
							
							MimeMessage message=new MimeMessage(Session.getDefaultInstance(new Properties()), new FileInputStream(storeFile));

							String sender=message.getHeader(FSQueueService.SENDER_HEADER)[0];
							String recipient=message.getHeader(FSQueueService.RECIPIENT_HEADER)[0];
							String outputService=message.getHeader(FSQueueService.OUTPUT_SERVICE_HEADER)[0];
							message.saveChanges();
							//if tmp should be in memory
							if(storeFile.length()<srv.getConfig().getDeferredSize()){
								os = new ByteArrayOutputStream();
								message.writeTo(os);
								os.flush();
								is = new ByteArrayInputStream(((ByteArrayOutputStream)os).toByteArray());
							//if tmp should be on disk
							}else{
								tmpFile = File.createTempFile(srv.getConfig().getTmpPrefix(), srv.getConfig().getSuffix(), srv.getConfig().getTmpPathFile());
								os = new FileOutputStream(tmpFile);
								message.writeTo(os);
								os.flush();
								is = new SharedTmpFileInputStream(tmpFile);
							}
							
							if(tmpFile!=null){
								fsMail.setTmpFile(tmpFile.getAbsolutePath());
							}else{
								fsMail.setTmpFile(null);
							}
							
							mail = MimeMail.createCustom(sender, recipient, 
									is, 
									outputService, 
									fsMail.getKey().getSequence(), 
									fsMail.getKey().getTime());
							srv.put(Mail.Phase.send, mail);
							log.info("mail again on queue "+mail);
						}catch (Exception e){
							log.error("inserting email again",e);
						}finally{
							if(os!=null){
								try{os.close();}catch(Exception e){}
							}
							if(is!=null){
								try{is.close();}catch(Exception e){}
							}
						}
					}
					//take it out of the store
					else{
						log.debug("taking email out of queue "+fsMail);
						File path = new File(srv.getConfig().getStorePath(),"error");
						path.mkdir();
						srv.saveToAndUnstore(fsMail.getKey().getTime(),fsMail.getKey().getSequence(), path.getAbsolutePath());
						log.info("mail out of queue "+fsMail);
					}
				}
			}
		}

	}
}
