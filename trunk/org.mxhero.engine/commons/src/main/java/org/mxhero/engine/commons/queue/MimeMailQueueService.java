package org.mxhero.engine.commons.queue;

import java.util.concurrent.TimeUnit;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.api.Mail;

public interface MimeMailQueueService {

	public MimeMail store(Mail.Phase phase, MimeMail mail, long timeout, TimeUnit unit)
    throws InterruptedException;
	
	public void unstore(MimeMail mail);
	
	public boolean offer(Mail.Phase phase, MimeMail mail, long timeout, TimeUnit unit)
    throws InterruptedException;
	
	public void put(Mail.Phase phase, MimeMail mail)
    throws InterruptedException;
	
	public void delayAndPut(Mail.Phase phase, MimeMail mail, long millisenconds)
	throws InterruptedException;
	
	public MimeMail poll(Mail.Phase phase, long timeout, TimeUnit unit) throws InterruptedException;

	public void saveToAndUnstore(MimeMail mail, String path, boolean useTmp);
	
	public void logState();
	
}
