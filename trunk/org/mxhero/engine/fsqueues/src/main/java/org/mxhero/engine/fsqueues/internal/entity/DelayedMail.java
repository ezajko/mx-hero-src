package org.mxhero.engine.fsqueues.internal.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.mxhero.engine.commons.mail.MimeMail;

public class DelayedMail implements Delayed{

	private MimeMail mail;
	
	private Long delay;
	private Date requestTime;
	private boolean noDelay=true;
	private Timestamp time;
	private Long sequence;
	

	public DelayedMail(Timestamp time, Long sequence) {
		this.time = time;
		this.sequence = sequence;
	}

	public DelayedMail(MimeMail mail) {
		this.mail = mail;
		this.time = mail.getTime();
		this.sequence = mail.getSequence();
		delay = System.currentTimeMillis();
		requestTime = new Date();
	}
	
	public DelayedMail(MimeMail mail, Long delay) {
		this.mail = mail;
		this.delay = System.currentTimeMillis()+delay;
		this.time = mail.getTime();
		this.sequence = mail.getSequence();
		requestTime = new Date();
		noDelay=false;
	}

	public int compareTo(Delayed o) {
		DelayedMail other = (DelayedMail)o;
		if (this.delay < other.delay)
	         return -1;
	      if (this.delay > other.delay)
	         return 1;
		return this.requestTime.compareTo(other.requestTime);
	}

	public long getDelay(TimeUnit unit) {
		if(noDelay){
			return 0;
		}
		return unit.convert(delay-System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}

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
