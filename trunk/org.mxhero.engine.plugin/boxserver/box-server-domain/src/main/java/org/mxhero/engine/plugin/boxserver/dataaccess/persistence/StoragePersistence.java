package org.mxhero.engine.plugin.boxserver.dataaccess.persistence;

import org.mxhero.engine.plugin.boxserver.Application;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.CreateUserResponse;
import org.mxhero.engine.plugin.boxserver.service.UserBox;

/**
 * The Interface StoragePersistence.
 */
public interface StoragePersistence {

	/**
	 * Store auth token.
	 *
	 * @param response the response
	 */
	public void storeAuthToken(UserBox response);

	/**
	 * Gets the account from storage.
	 *
	 * @param email the email
	 * @return the account from storage
	 */
	public CreateUserResponse getAccountFromStorage(String email);

	/**
	 * Gets the app from key.
	 *
	 * @param appKey the app key
	 * @return the app from key
	 */
	public Application getAppFromKey(String appKey);

	/**
	 * Register new app.
	 *
	 * @param name the name
	 */
	public void registerNewApp(String name);

}