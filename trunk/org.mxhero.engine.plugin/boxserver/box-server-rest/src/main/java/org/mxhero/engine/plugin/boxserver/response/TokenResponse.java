package org.mxhero.engine.plugin.boxserver.response;

import java.io.Serializable;

import org.mxhero.engine.plugin.boxserver.service.UserBox;

/**
 * The Class TokenResponse.
 */
public class TokenResponse implements Serializable{
	
	/** The email. */
	private String email;
	
	/** The token. */
	private String token;
	
	/** The error. */
	private ErrorResponse error;
	
	/**
	 * Instantiates a new token response.
	 */
	public TokenResponse() {
	}
	
	/**
	 * Instantiates a new token response.
	 *
	 * @param token the user box
	 */
	public TokenResponse(UserBox token){
		this.email = token.getEmail();
		this.token = token.getToken();
		if(!token.wasCreatedOk()){
			this.error = new ErrorResponse(token.getResponseMessage());
		}
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

}
