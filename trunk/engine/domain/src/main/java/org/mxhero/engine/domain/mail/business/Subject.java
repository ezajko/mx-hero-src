package org.mxhero.engine.domain.mail.business;

/**
 * Represents the subject of a mail so it can be used inside rules.
 * @author mmarmol
 */
public class Subject {

	private String subject;

	public Subject() {
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

}
