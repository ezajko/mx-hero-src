/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mxhero.engine.commons.mail;

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
