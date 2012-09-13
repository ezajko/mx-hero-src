/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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

/**
 * @author mmarmol
 *
 */
public class FSLoader {

	private static Logger log = LoggerFactory.getLogger(FSLoader.class);
	private long checkTime=10000;
	private FSConfig config;
	private boolean weekWorking=true;
	private FSQueueService queueService;
	
	/**
	 * 
	 */
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
	
	/**
	 * 
	 */
	public void stop(){
		weekWorking=false;
	}
	
	/**
	 * 
	 */
	private void work(){
		for(Mail.Phase phase : Mail.Phase.values()){
			loadPhase(phase);
		}
	}

	/**
	 * @param phase
	 */
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
					String forcedPriority = null;
					MimeMail mail = null;

					try{
						File tmpFile = File.createTempFile("load", ".eml",config.getTmpPathFile());
						Files.copy(mailFile, tmpFile);
						SharedTmpFileInputStream is = new SharedTmpFileInputStream(tmpFile);
						data=new MimeMessage(Session.getDefaultInstance(new Properties()), is);
						sender=data.getHeader(FSQueueService.SENDER_HEADER)[0];
						recipient=data.getHeader(FSQueueService.RECIPIENT_HEADER)[0];
						outputService=data.getHeader(FSQueueService.OUTPUT_SERVICE_HEADER)[0];
						if(data.getHeader(FSQueueService.FORCED_PRIORITY_HEADER)!=null){
							forcedPriority=data.getHeader(FSQueueService.FORCED_PRIORITY_HEADER)[0];	
						}
						log.trace("priority="+forcedPriority);
						data.removeHeader(FSQueueService.SENDER_HEADER);
						data.removeHeader(FSQueueService.RECIPIENT_HEADER);
						data.removeHeader(FSQueueService.OUTPUT_SERVICE_HEADER);
						data.removeHeader(FSQueueService.FORCED_PRIORITY_HEADER);
						is.close();
						is = new SharedTmpFileInputStream(tmpFile);
						mail= new MimeMail(sender, recipient, is, outputService);
						mail.setPhase(phase);
						try{mail.setForcedPhasePriority(Long.parseLong(forcedPriority));}catch(Exception e){}
						log.trace("forcedPriority="+mail.getForcedPhasePriority());
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

	/**
	 * @return
	 */
	public FSQueueService getQueueService() {
		return queueService;
	}

	/**
	 * @param queueService
	 */
	public void setQueueService(FSQueueService queueService) {
		this.queueService = queueService;
	}

	/**
	 * @return
	 */
	public FSConfig getConfig() {
		return config;
	}

	/**
	 * @param config
	 */
	public void setConfig(FSConfig config) {
		this.config = config;
	}

}
