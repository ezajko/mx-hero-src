package org.mxhero.engine.domain.mail;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Class used to generate sequence for mails.
 * @author mmarmol
 */
public final class Sequencer {

	private static final long MIN_VALUE=0;
	
	private AtomicLong longSequence = new AtomicLong();
	
	private static Sequencer instance = new Sequencer();

	
	/**
	 * @return
	 */
	public static Sequencer getInstance(){
		return instance;
	}
	
	/**
	 * Used to return the next sequence.
	 * @return
	 */
	public synchronized Long getNextSequence(){
		Long sequence = longSequence.getAndIncrement();
			if(sequence<MIN_VALUE){
				longSequence.set(MIN_VALUE);
				sequence = longSequence.getAndIncrement();
			}
		return sequence;
	}
}
