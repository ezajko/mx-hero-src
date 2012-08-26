package org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.persistence;

import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response.CreateTokenResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.service.UserBoxClient;

/**
 * The Interface ClientStoragePersistence.
 */
public interface ClientStoragePersistence {

	/**
	 * Gets the account from storage.
	 *
	 * @param email the email
	 * @return the account from storage
	 */
	public CreateTokenResponse getAccountFromStorage(String email);

	/**
	 * Store token.
	 *
	 * @param userBoxClient the user box client
	 */
	public void storeToken(UserBoxClient userBoxClient);

	/**
	 * Register store file.
	 *
	 * @param userBoxClient the user box client
	 * @param filePath the file path
	 */
	public void registerStoreFile(UserBoxClient userBoxClient, String filePath);

	/**
	 * Checks for been proccessed.
	 *
	 * @param filePath the file path
	 * @return true, if successful
	 */
	public boolean hasBeenProccessed(String filePath);


}