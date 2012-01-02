package org.mxhero.engine.commons.mail.business;

import java.util.Date;
import java.util.List;

/**
 * Represents data that wont change in a specific phase of a mail processing.
 * @author mmarmol
 */
public class InitialData {

	private int initialSize;

	private User sender;
	
	private User fromSender;
	
	private User recipient;
	
	private List<User> recipientsInHeaders;

	private Date sentDate;
	
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

	public User getFromSender() {
		return fromSender;
	}

	public void setFromSender(User fromSender) {
		this.fromSender = fromSender;
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

	public List<User> getRecipientsInHeaders() {
		return recipientsInHeaders;
	}

	public void setRecipientsInHeaders(List<User> recipientsInHeaders) {
		this.recipientsInHeaders = recipientsInHeaders;
	}

}
