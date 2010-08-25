package org.mxhero.engine.domain.mail.business;

import java.util.Date;

/**
 * Represents data that wont change in a specific phase of a mail processing.
 * @author mmarmol
 */
public class InitialData {

	private int initialSize;

	private String initialSender;

	private String initialRecipient;
	
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
	 * @return the initialSender
	 */
	public String getInitialSender() {
		return initialSender;
	}

	/**
	 * @param initialSender the initialSender to set
	 */
	public void setInitialSender(String initialSender) {
		this.initialSender = initialSender;
	}

	/**
	 * @return the initialRecipient
	 */
	public String getInitialRecipient() {
		return initialRecipient;
	}

	/**
	 * @param initialRecipient the initialRecipient to set
	 */
	public void setInitialRecipient(String initialRecipient) {
		this.initialRecipient = initialRecipient;
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
