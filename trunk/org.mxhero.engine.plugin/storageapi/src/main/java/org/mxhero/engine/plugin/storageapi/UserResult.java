package org.mxhero.engine.plugin.storageapi;


/**
 * The Class UserResult.
 */
public class UserResult {
	
	/** The email. */
	private String email;
	
	/** The sucess. */
	private boolean sucess;
	
	/** The already exist. */
	private boolean alreadyExist;
	
	/** The sender. */
	private boolean sender;
	
	/** The message. */
	private UserResultMessage message;

	/** The body. */
	private String body;

	/**
	 * Instantiates a new user result.
	 *
	 * @param success the success
	 * @param message the message
	 */
	public UserResult(boolean success, UserResultMessage message) {
		this.sucess = success;
		this.message = message;
	}

	/**
	 * Instantiates a new user result.
	 *
	 * @param email the email
	 */
	public UserResult(String email) {
		this.sucess = true;
		this.email = email;
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

	/**
	 * Checks if is sucess.
	 *
	 * @return true, if is sucess
	 */
	public boolean isSucess() {
		return sucess;
	}

	/**
	 * Sets the sucess.
	 *
	 * @param sucess the new sucess
	 */
	public void setSucess(boolean sucess) {
		this.sucess = sucess;
	}

	/**
	 * Checks if is already exist.
	 *
	 * @return true, if is already exist
	 */
	public boolean isAlreadyExist() {
		return alreadyExist;
	}

	/**
	 * Sets the already exist.
	 *
	 * @param alreadyExist the new already exist
	 */
	public void setAlreadyExist(boolean alreadyExist) {
		this.alreadyExist = alreadyExist;
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
	 * Gets the message.
	 *
	 * @return the message
	 */
	public UserResultMessage getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(UserResultMessage message) {
		this.message = message;
	}

	/**
	 * Gets the result type.
	 *
	 * @return the result type
	 */
	public UserResulType getResultType() {
		if(isSender()){
			return UserResulType.SENDER;
		}else{
			return UserResulType.RECIPIENT;
		}
	}

	/**
	 * Gets the body.
	 *
	 * @return the body
	 */
	public String getBody() {
		return this.body;
	}
	
	/**
	 * Sets the body.
	 *
	 * @param body the new body
	 */
	public void setBody(String body) {
		this.body = body;
	}

}
