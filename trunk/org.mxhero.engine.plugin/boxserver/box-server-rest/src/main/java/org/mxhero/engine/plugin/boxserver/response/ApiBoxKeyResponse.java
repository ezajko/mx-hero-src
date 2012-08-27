package org.mxhero.engine.plugin.boxserver.response;

import java.io.Serializable;

/**
 * The Class ApiBoxKeyResponse.
 */
public class ApiBoxKeyResponse implements Serializable{
	
	/** The api key. */
	private String apiKey;

	/**
	 * Gets the api key.
	 *
	 * @return the api key
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * Sets the api key.
	 *
	 * @param apiKey the new api key
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

}
