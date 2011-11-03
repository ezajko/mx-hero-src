package org.mxhero.engine.domain.queue;

import java.util.concurrent.TimeUnit;

import org.mxhero.engine.domain.mail.MimeMail;


public interface MimeMailQueueService {

	public boolean store(String phase, MimeMail mail, long timeout, TimeUnit unit)
    throws InterruptedException;
	
	public void unstore(MimeMail mail);
	
	public boolean offer(String phase, MimeMail mail, long timeout, TimeUnit unit)
    throws InterruptedException;
	
	public void put(String phase, MimeMail mail)
    throws InterruptedException;
	
	public void delayAndPut(String phase, MimeMail mail, long millisenconds)
	throws InterruptedException;
	
	public MimeMail poll(String phase, long timeout, TimeUnit unit) throws InterruptedException;

	public void saveToAndUnstore(MimeMail mail, String path, boolean useTmp);
	
	public void logState();
	
}