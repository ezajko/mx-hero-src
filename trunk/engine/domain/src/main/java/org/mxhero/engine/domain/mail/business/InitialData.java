package org.mxhero.engine.domain.mail.business;

import java.util.Date;

/**
 * Represents data that wont change in a specific phase of a mail processing.
 * @author mmarmol
 */
public class InitialData {

	private int initialSize;

	private User sender;
	
	private User from;
	
	private User recipient;

	private Date sentDate;
	
	private Date receivedDate;
	
	private String phase = RulePhase.SEND;

	/**
	 * @return the initialSize
	 */
	public int getInitialSize() {
		return initialSize;
	}

	/**
	 * @param initialSize the initialSize to set
	 */
	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}

	/**
	 * @return
	 */
	public User getSender() {
		return sender;
	}

	/**
	 * @param sender
	 */
	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getFrom() {
		return from;
	}

	public void setFrom(User from) {
		this.from = from;
	}

	/**
	 * @return
	 */
	public User getRecipient() {
		return recipient;
	}

	/**
	 * @param recipient
	 */
	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}

	/**
	 * @return the sentDate
	 */
	public Date getSentDate() {
		return sentDate;
	}

	/**
	 * @param sentDate the sentDate to set
	 */
	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	/**
	 * @return the receivedDate
	 */
	public Date getReceivedDate() {
		return receivedDate;
	}

	/**
	 * @param receivedDate the receivedDate to set
	 */
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	/**
	 * @return the phase
	 */
	public String getPhase() {
		return phase;
	}

	/**
	 * @param phase the phase to set
	 */
	public void setPhase(String phase) {
		this.phase = phase;
	}

}
