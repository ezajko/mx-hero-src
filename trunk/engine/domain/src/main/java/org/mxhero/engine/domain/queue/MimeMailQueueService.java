package org.mxhero.engine.domain.queue;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.mxhero.engine.domain.mail.MimeMail;

public interface MimeMailQueueService {

	void put(String module, String phase, MimeMail e) throws InterruptedException;
	
	boolean offer(String module, String phase, MimeMail e, long timeout, TimeUnit unit)
    throws InterruptedException;
	
	boolean offer(String module, String phase, MimeMail e);
	
	MimeMail take(String module, String phase) throws InterruptedException;
	
	MimeMail poll(String module, String phase, long timeout, TimeUnit unit) throws InterruptedException;
	
	MimeMail poll(String module, String phase);
	
	boolean remove(String module, String phase, MimeMail o) throws InterruptedException;
	
	void removeAddTo(String fromModule, String fromPhase, MimeMail o, MimeMail z, String toModule, String toPhase) throws InterruptedException;
	
	void reEnqueue(String module, String phase, MimeMail o) throws InterruptedException;
	
	Map<String, String>getQueuesCount();
	
	void logState();
}
