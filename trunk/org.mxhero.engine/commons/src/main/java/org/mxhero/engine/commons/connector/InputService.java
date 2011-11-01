package org.mxhero.engine.commons.connector;

import org.mxhero.engine.commons.mail.MimeMail;


/**
 * This interface is used by Core module to implement a service where any connector can use to add mails to be processed.
 * @author mmarmol
 */
public interface InputService {

	/**
	 * This method is used to add mails to the processing queue.
	 * 
	 * @param mail is the of the mail
	 * @throws IllegalArgumentException may throw this exception is any of the parameters are null.
	 */
	void addMail(MimeMail mail) throws QueueFullException;
	
}
