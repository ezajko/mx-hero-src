package org.mxhero.engine.fsqueues.internal.entity;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.mxhero.engine.domain.mail.MimeMail;

public class DelayedMail implements Delayed{

	private MimeMail mail;
	
	private Long delay = 0l;
	
	public DelayedMail(MimeMail mail) {
		this.mail = mail;
	}
	
	public DelayedMail(MimeMail mail, Long delay) {
		this.mail = mail;
		this.delay = delay;
	}

	public int compareTo(Delayed o) {
		return delay.compareTo(o.getDelay(TimeUnit.MILLISECONDS));
	}

	public long getDelay(TimeUnit unit) {
		return unit.convert(delay, TimeUnit.MILLISECONDS);
	}

	public MimeMail getMail() {
		return mail;
	}
}
