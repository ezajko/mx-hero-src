package org.mxhero.engine.commons.queue;

import java.util.concurrent.TimeUnit;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.api.Mail;

/**
 * @author mmarmol
 *
 */
public interface MimeMailQueueService {

	/**
	 * @param phase
	 * @param mail
	 * @param timeout
	 * @param unit
	 * @return
	 * @throws InterruptedException
	 */
	public MimeMail store(Mail.Phase phase, MimeMail mail, long timeout, TimeUnit unit)
    throws InterruptedException;
	
	/**
	 * @param mail
	 */
	public void unstore(MimeMail mail);
	
	/**
	 * @param phase
	 * @param mail
	 * @param timeout
	 * @param unit
	 * @return
	 * @throws InterruptedException
	 */
	public boolean offer(Mail.Phase phase, MimeMail mail, long timeout, TimeUnit unit)
    throws InterruptedException;
	
	/**
	 * @param phase
	 * @param mail
	 * @throws InterruptedException
	 */
	public void put(Mail.Phase phase, MimeMail mail)
    throws InterruptedException;
	
	/**
	 * @param phase
	 * @param mail
	 * @param millisenconds
	 * @throws InterruptedException
	 */
	public void delayAndPut(Mail.Phase phase, MimeMail mail, long millisenconds)
	throws InterruptedException;
	
	/**
	 * @param phase
	 * @param timeout
	 * @param unit
	 * @return
	 * @throws InterruptedException
	 */
	public MimeMail poll(Mail.Phase phase, long timeout, TimeUnit unit) throws InterruptedException;

	/**
	 * @param mail
	 * @param path
	 * @param useTmp
	 */
	public void saveToAndUnstore(MimeMail mail, String path, boolean useTmp);
	
	/**
	 * 
	 */
	public void logState();
	
}
