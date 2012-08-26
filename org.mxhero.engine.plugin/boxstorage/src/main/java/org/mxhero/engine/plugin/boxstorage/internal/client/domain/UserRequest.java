package org.mxhero.engine.plugin.boxstorage.internal.client.domain;


/**
 * The Class UserRequest.
 */
public class UserRequest {

	/** The sender. */
	private boolean sender;
	
	/** The recipient. */
	private boolean recipient;

	/** The email. */
	private String email;

	/**
	 * Instantiates a new user request.
	 *
	 * @param email the email
	 * @param sender the sender
	 */
	public UserRequest(String email, boolean sender) {
		this.email = email;
		setSender(sender);
		setRecipient(!isSender());
	}

	/**
	 * Checks if is sender.
	 *
	 * @return true, if is sender
	 */
	public boolean isSender() {
		return sender;
	}

	/**
	 * Sets the sender.
	 *
	 * @param sender the new sender
	 */
	public void setSender(boolean sender) {
		this.sender = sender;
	}

	/**
	 * Checks if is recipient.
	 *
	 * @return true, if is recipient
	 */
	public boolean isRecipient() {
		return recipient;
	}

	/**
	 * Sets the recipient.
	 *
	 * @param recipient the new recipient
	 */
	public void setRecipient(boolean recipient) {
		this.recipient = recipient;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	
	
}
