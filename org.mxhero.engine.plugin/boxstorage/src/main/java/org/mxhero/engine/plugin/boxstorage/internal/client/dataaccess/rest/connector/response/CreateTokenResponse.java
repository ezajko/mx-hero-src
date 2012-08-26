package org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response;

import java.io.Serializable;

import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.CodeResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.Item;

/**
 * The Class CreateTokenResponse.
 */
public class CreateTokenResponse implements Serializable {
	
	/** The has previous account. */
	private boolean previousAccount;
	
	/** The email. */
	private String email;
	
	/** The token. */
	private String token;

	/** The error. */
	private ErrorResponse error;

	/** The item. */
	private Item item;

	/**
	 * Instantiates a new creates the token response.
	 */
	public CreateTokenResponse() {
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

	/**
	 * Sets the item.
	 *
	 * @param item the new item
	 */
	public void setItem(Item item) {
		this.item = item;
	}
	
	/**
	 * Gets the item.
	 *
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * Was response ok.
	 *
	 * @return true, if successful
	 */
	public boolean wasResponseOk() {
		return error == null || !error.isError();
	}

	
	/**
	 * Already exist.
	 *
	 * @return true, if successful
	 */
	public boolean alreadyExist() {
		return error!=null && CodeResponse.USER_ALREADY_EXIST.getMessage().equals(error.getMessage());
	}

}
