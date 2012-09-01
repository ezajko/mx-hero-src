package org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response;

import java.io.Serializable;

import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.AbstractResponse;

/**
 * The Class CreateKeyResponse.
 */
public class CreateKeyResponse extends AbstractResponse{
	
	/** The app key. */
	private String appKey;
	
	/** The error. */
	private ErrorResponse error;
	
	/**
	 * Instantiates a new creates the key response.
	 */
	public CreateKeyResponse() {
	}

	/**
	 * Instantiates a new creates the key response.
	 *
	 * @param appKey2 the app key2
	 */
	public CreateKeyResponse(String appKey2) {
		this.appKey = appKey2;
	}

	/**
	 * Gets the app key.
	 *
	 * @return the app key
	 */
	public String getAppKey() {
		return appKey;
	}

	/**
	 * Sets the app key.
	 *
	 * @param appKey the new app key
	 */
	public void setAppKey(String appKey) {
		this.appKey = appKey;
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
