package org.mxhero.engine.domain.mail.business;

/**
 * Represents the sender of a mail so it can be used inside rules.
 * @author mmarmol
 */
public class Sender {

	private String sender;
	
	private String from;

	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

}
