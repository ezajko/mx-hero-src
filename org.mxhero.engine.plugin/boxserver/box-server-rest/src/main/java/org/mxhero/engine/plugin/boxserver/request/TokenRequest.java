package org.mxhero.engine.plugin.boxserver.request;

import java.io.Serializable;

/**
 * The Class TokenRequest.
 */
public class TokenRequest implements Serializable{

	/** The email. */
	private String email;

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
