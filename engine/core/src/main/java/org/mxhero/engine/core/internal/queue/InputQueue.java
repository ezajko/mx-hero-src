package org.mxhero.engine.core.internal.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.mxhero.engine.domain.mail.MimeMail;

/**
 * This is an abstract class using a singleton pattern to hold the reference
 * to the queue used to hold mails that are going to be processed.
 * @author mmarmol
 */
public abstract class InputQueue {

	private static BlockingQueue<MimeMail> queue = new LinkedBlockingQueue<MimeMail>() ;
	
	/**
	 * Use this class to return the instance of the queue.
	 * @return the instance of the queue.
	 */
	public static BlockingQueue<MimeMail> getInstance(){
		return queue;
	}
	
	/**
	 * Can not be instantiated.
	 */
	private InputQueue(){
	}
	
}
