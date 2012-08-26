package org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain;

import org.codehaus.jackson.annotate.JsonAnySetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class AbstractResponse.
 */
public abstract class AbstractResponse {
	
	/** The logger. */
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/** The response. */
	private CodeResponse response;
	
	/**
	 * Sets the any.
	 *
	 * @param key the key
	 * @param value the value
	 */
	@JsonAnySetter
	public void setAny(String key, Object value){
		logger.warn("NEW PROPERTY HAS BEEN SENT FROM BOX AND IT IS NOT MAPPED BY US [Property {} - Value {}]", key, value);
	}
	
	/**
	 * Sets the response.
	 *
	 * @param response the new response
	 */
	public void setResponse(CodeResponse response) {
		this.response = response;
		
	}

	/**
	 * Gets the response.
	 *
	 * @return the response
	 */
	public CodeResponse getResponse() {
		return response;
	}

	/**
	 * Was response ok.
	 *
	 * @return true, if successful
	 */
	public boolean wasResponseOk() {
		return CodeResponse.OK.equals(getResponse());
	}

}
