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

package org.mxhero.engine.fsqueues.internal.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.mxhero.engine.commons.mail.MimeMail;

/**
 * @author mmarmol
 *
 */
public class DelayedMail implements Delayed{

	private MimeMail mail;
	
	private Long delay;
	private Date requestTime;
	private boolean noDelay=true;
	private Timestamp time;
	private Long sequence;
	

	/**
	 * @param time
	 * @param sequence
	 */
	public DelayedMail(Timestamp time, Long sequence) {
		this.time = time;
		this.sequence = sequence;
	}

	/**
	 * @param mail
	 */
	public DelayedMail(MimeMail mail) {
		this.mail = mail;
		this.time = mail.getTime();
		this.sequence = mail.getSequence();
		delay = System.currentTimeMillis();
		requestTime = new Date();
	}
	
	/**
	 * @param mail
	 * @param delay
	 */
	public DelayedMail(MimeMail mail, Long delay) {
		this.mail = mail;
		this.delay = System.currentTimeMillis()+delay;
		this.time = mail.getTime();
		this.sequence = mail.getSequence();
		requestTime = new Date();
		noDelay=false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Delayed o) {
		DelayedMail other = (DelayedMail)o;
		if (this.delay < other.delay)
	         return -1;
	      if (this.delay > other.delay)
	         return 1;
		return this.requestTime.compareTo(other.requestTime);
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Delayed#getDelay(java.util.concurrent.TimeUnit)
	 */
	public long getDelay(TimeUnit unit) {
		if(noDelay){
			return 0;
		}
		return unit.convert(delay-System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}

	/**
	 * @return
	 */
	public MimeMail getMail() {
		return mail;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sequence == null) ? 0 : sequence.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DelayedMail other = (DelayedMail) obj;
		if (sequence == null) {
			if (other.sequence != null)
				return false;
		} else if (!sequence.equals(other.sequence))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}

	
}
