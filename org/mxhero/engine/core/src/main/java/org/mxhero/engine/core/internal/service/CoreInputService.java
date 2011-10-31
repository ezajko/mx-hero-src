package org.mxhero.engine.core.internal.service;

import java.util.concurrent.TimeUnit;

import org.mxhero.engine.commons.connector.InputService;
import org.mxhero.engine.commons.connector.QueueFullException;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.queue.MimeMailQueueService;
import org.mxhero.engine.core.internal.CoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This implementation takes the parameters and creates a new Mail
 * assigning a sequence and adding the requirement into the input queue.
 * @author mmarmol
 * @see org.mxhero.engine.domain.connector.InputService
 */
public final class CoreInputService implements InputService {

	private static Logger log = LoggerFactory.getLogger(CoreInputService.class);
	
	private static final long WAIT_TIME = 5000;
	
	private MimeMailQueueService queueService;
	
	private CoreProperties properties;
	
	/**
	 * @see org.mxhero.engine.domain.connector.InputService#addMail(byte[], java.lang.String)
	 */
	public void addMail(MimeMail mail) throws QueueFullException{
		boolean added = false;
		if (mail==null || mail.getResponseServiceId()==null){
			throw new IllegalArgumentException("mail:"+mail);
		}
		
		try {
			added = queueService.store( mail.getPhase(), mail,WAIT_TIME, TimeUnit.MILLISECONDS );
		} catch (InterruptedException e) {
			log.error("interrupted while waiting:"+mail,e);
		}
		if(!added){
			throw new QueueFullException();
		}
		log.info("STORED "+mail);
		queueService.logState();
	}

	public MimeMailQueueService getQueueService() {
		return queueService;
	}

	public void setQueueService(MimeMailQueueService queueService) {
		this.queueService = queueService;
	}

	public CoreProperties getProperties() {
		return properties;
	}

	public void setProperties(CoreProperties properties) {
		this.properties = properties;
	}

}
