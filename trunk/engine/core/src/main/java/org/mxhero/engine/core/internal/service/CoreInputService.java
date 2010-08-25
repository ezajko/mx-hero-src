package org.mxhero.engine.core.internal.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.mxhero.engine.core.internal.queue.InputQueue;
import org.mxhero.engine.domain.connector.InputService;
import org.mxhero.engine.domain.mail.MimeMail;
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
	
	private BlockingQueue<MimeMail> queue = InputQueue.getInstance();
	
	/**
	 * @see org.mxhero.engine.domain.connector.InputService#addMail(byte[], java.lang.String)
	 */
	public void addMail(MimeMail mail) {
		boolean keepTrying = true;
		if (mail==null || mail.getResponseServiceId()==null){
			throw new IllegalArgumentException("mail:"+mail);
		}
		log.debug("Adding email:[rawEmail:"+mail+"],[reponseServiceId:"+mail.getResponseServiceId()+"]");
		while(keepTrying){
			try {
				/*if offer returns queue we need to stop*/
				keepTrying=!queue.offer(mail, WAIT_TIME, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				log.warn(InputQueue.class.getName()+" is full, waiting for space to become available.");
			}
		}
		log.debug("Mail added to queue:"+mail);
	}

}
