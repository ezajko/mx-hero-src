package org.mxhero.engine.fsqueues.internal.loader;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.fsqueues.internal.FSConfig;
import org.mxhero.engine.fsqueues.internal.FSQueueService;
import org.mxhero.engine.fsqueues.internal.util.Files;
import org.mxhero.engine.fsqueues.internal.util.SharedTmpFileInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FSLoader {

	private static Logger log = LoggerFactory.getLogger(FSLoader.class);
	private long checkTime=10000;
	private FSConfig config;
	private boolean weekWorking=true;
	private FSQueueService queueService;
	
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
						log.error("error while working",e);
					}
				}			
			}
		}).start();
	}
	
	public void stop(){
		weekWorking=false;
	}
	
	private void work(){
		for(Mail.Phase phase : Mail.Phase.values()){
			loadPhase(phase);
		}
	}

	private void loadPhase(Mail.Phase phase){
		try{
			File pathFile = new File(config.getLoadPathFile(),phase.name());
			if(pathFile.exists() && pathFile.isDirectory()){
				File[] mailFiles = pathFile.listFiles();
				for(File mailFile : mailFiles){
					MimeMessage data = null;
					String recipient = null;
					String sender = null;
					String outputService = null;
					MimeMail mail = null;

					try{
						File tmpFile = File.createTempFile("load", ".eml",config.getTmpPathFile());
						Files.copy(mailFile, tmpFile);
						SharedTmpFileInputStream is = new SharedTmpFileInputStream(tmpFile);
						data=new MimeMessage(Session.getDefaultInstance(new Properties()), is);
						sender=data.getHeader(FSQueueService.SENDER_HEADER)[0];
						recipient=data.getHeader(FSQueueService.RECIPIENT_HEADER)[0];
						outputService=data.getHeader(FSQueueService.OUTPUT_SERVICE_HEADER)[0];
						is.close();
						is = new SharedTmpFileInputStream(tmpFile);
						mail= new MimeMail(sender, recipient, is, outputService);
						mail.setPhase(phase);
						queueService.store(phase, mail, 1000, TimeUnit.MILLISECONDS);
						is.close();
						mailFile.delete();
					}catch(Exception e){
						log.error("error loading email "+mailFile.getAbsolutePath(),e);
						continue;
					}
				}
			}
		}catch(Exception e){
			log.error("error loading phase "+phase,e);
		}
	}

	public FSQueueService getQueueService() {
		return queueService;
	}

	public void setQueueService(FSQueueService queueService) {
		this.queueService = queueService;
	}

	public FSConfig getConfig() {
		return config;
	}

	public void setConfig(FSConfig config) {
		this.config = config;
	}

}
