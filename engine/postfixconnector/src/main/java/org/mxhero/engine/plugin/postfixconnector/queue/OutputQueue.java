package org.mxhero.engine.plugin.postfixconnector.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.mxhero.engine.domain.mail.MimeMail;

/**
 * Singleton class for the Output queue of mails.
 * @author mmarmol
 */
public abstract class OutputQueue {

	private static BlockingQueue<MimeMail> queue = new  LinkedBlockingQueue<MimeMail>() ;
	
	/**
	 * Use this method to return the instance of the queue.
	 * @return
	 */
	public static BlockingQueue<MimeMail> getInstance(){
		return queue;
	}
	
	/**
	 * Private Constructor
	 */
	private OutputQueue(){
	}
}
