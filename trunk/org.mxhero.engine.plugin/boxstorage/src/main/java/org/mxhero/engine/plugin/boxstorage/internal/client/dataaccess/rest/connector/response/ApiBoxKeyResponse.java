package org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response;

import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.AbstractResponse;

/**
 * The Class ApiBoxKeyResponse.
 */
public class ApiBoxKeyResponse extends AbstractResponse{
	
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
