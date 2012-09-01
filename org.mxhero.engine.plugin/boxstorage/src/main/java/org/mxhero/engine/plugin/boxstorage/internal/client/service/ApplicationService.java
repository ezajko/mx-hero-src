package org.mxhero.engine.plugin.boxstorage.internal.client.service;

import org.apache.commons.lang.StringUtils;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.BoxApi;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response.CreateKeyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ApplicationService.
 */
public class ApplicationService {
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(ApplicationService.class);

	
	/** The connector. */
	private BoxApi connector;
	
	/** The application id. */
	private String applicationId;

	/**
	 * Gets the connector.
	 *
	 * @return the connector
	 */
	public BoxApi getConnector() {
		return connector;
	}

	/**
	 * Sets the connector.
	 *
	 * @param connector the new connector
	 */
	public void setConnector(BoxApi connector) {
		this.connector = connector;
	}

	/**
	 * Inits the app key.
	 */
	public void initAppKey() {
		logger.debug("Init application key to interact with mxhero box server");
		String appKey = ApplicationKey.getKey();
		if(StringUtils.isEmpty(appKey)){
			logger.debug("Application key is not cached. Requesting app key to server");
			CreateKeyResponse appKeyResp = connector.getAppKey(getApplicationId());
			if(!appKeyResp.wasResponseOk()){
				throw new RuntimeException("Server mxhero Box could not retrieve App key to interact with it.");
			}
			appKey = appKeyResp.getAppKey();
			ApplicationKey.setKey(appKey);
		}
	}

	/**
	 * Gets the application id.
	 *
	 * @return the application id
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * Sets the application id.
	 *
	 * @param applicationId the new application id
	 */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * Inits the api box key.
	 */
	public void initApiBoxKey() {
		connector.getApiKey();
	}


}
