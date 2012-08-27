package org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest;

import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.FileUploadResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.Item;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.ItemResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response.CreateTokenResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.service.UserBoxClient;

// TODO: Auto-generated Javadoc
/**
 * The Interface BoxApi.
 */
public interface BoxApi {
	
	/**
	 * Creates the account.
	 *
	 * @param email the email
	 * @return the creates the user response
	 */
	public CreateTokenResponse createAccount(String email);
	
	/**
	 * Store.
	 *
	 * @param userBox the user box
	 * @param filePath the files path
	 * @return the storage result
	 */
	public FileUploadResponse store(UserBoxClient userBox, String filePath);

	/**
	 * Gets the folder mx hero.
	 *
	 * @param userBox the user box
	 * @return the folder mx hero
	 */
	public ItemResponse getFolderMxHero(UserBoxClient userBox);

	/**
	 * Creates the mx hero folder.
	 *
	 * @param userBox the user box
	 * @return the item
	 */
	public Item createMxHeroFolder(UserBoxClient userBox);

	/**
	 * Gets the app key.
	 *
	 * @param applicationId the application id
	 * @return the app key
	 */
	public String getAppKey(String applicationId);

	/**
	 * Share file.
	 *
	 * @param userBoxClient the user box client
	 */
	public void shareFile(UserBoxClient userBoxClient);

	/**
	 * Gets the box api key.
	 *
	 * @return the box api key
	 */
	public String getBoxApiKey();

}
