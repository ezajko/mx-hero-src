package org.mxhero.engine.plugin.boxserver.response;

import java.io.Serializable;

import org.mxhero.engine.plugin.boxserver.service.UserBox;

/**
 * The Class CreateTokenResponse.
 */
public class CreateTokenResponse implements Serializable {
	
	/** The has previous account. */
	private boolean previousAccount;
	
	/** The email. */
	private String email;

	/** The error. */
	private ErrorResponse error;

	/** The token. */
	private String token;

	/**
	 * Instantiates a new creates the token response.
	 */
	public CreateTokenResponse() {
	}
	
	/**
	 * Instantiates a new creates the token response.
	 *
	 * @param createAccount the user box
	 */
	public CreateTokenResponse(UserBox createAccount) {
		setEmail(createAccount.getEmail());
		setPreviousAccount(createAccount.hasPreviousAccount());
		if(!createAccount.wasCreatedOk()){
			error = new ErrorResponse(createAccount.getResponseMessage());
		}else{
			this.setToken(createAccount.getToken());
		}
	}

	/**
	 * Checks if is checks for previous account.
	 *
	 * @return true, if is checks for previous account
	 */
	public boolean isPreviousAccount() {
		return previousAccount;
	}

	/**
	 * Sets the checks for previous account.
	 *
	 * @param previousAccount the new checks for previous account
	 */
	public void setPreviousAccount(boolean previousAccount) {
		this.previousAccount = previousAccount;
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
	 * Gets the error.
	 *
	 * @return the error
	 */
	public ErrorResponse getError() {
		return error;
	}

	/**
	 * Sets the error.
	 *
	 * @param error the new error
	 */
	public void setError(ErrorResponse error) {
		this.error = error;
	}

	/**
	 * Gets the token.
	 *
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Sets the token.
	 *
	 * @param token the new token
	 */
	public void setToken(String token) {
		this.token = token;
	}

}
