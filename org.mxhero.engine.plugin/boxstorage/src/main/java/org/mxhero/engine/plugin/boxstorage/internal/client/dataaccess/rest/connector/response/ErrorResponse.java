package org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response;

import java.io.Serializable;

/**
 * The Class ErrorResponse.
 */
public class ErrorResponse implements Serializable {
	
	/** The error. */
	private boolean error;
	
	/** The message. */
	private String message;
	
	/**
	 * Instantiates a new error response.
	 */
	public ErrorResponse(){}
	
	/**
	 * Instantiates a new error response.
	 *
	 * @param message the message
	 */
	public ErrorResponse(String message){
		this.error = true;
		this.message = message;
	}
	
	/**
	 * Checks if is error.
	 *
	 * @return true, if is error
	 */
	public boolean isError() {
		return error;
	}
	
	/**
	 * Sets the error.
	 *
	 * @param error the new error
	 */
	public void setError(boolean error) {
		this.error = error;
	}
	
	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
