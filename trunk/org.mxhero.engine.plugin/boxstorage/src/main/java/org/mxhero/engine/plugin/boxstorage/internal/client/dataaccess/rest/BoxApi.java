package org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest;

import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.FileUploadResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.Item;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.ItemResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response.ApiBoxKeyResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response.CreateKeyResponse;
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
	 * Gets the app key.
	 *
	 * @param applicationId the application id
	 * @return the app key
	 */
	public CreateKeyResponse getAppKey(String applicationId);

	/**
	 * Share file.
	 *
	 * @param userBoxClient the user box client
	 * @param fileName 
	 */
	public void updateFileInfo(UserBoxClient userBoxClient, String fileName);

	/**
	 * Gets the box api key.
	 *
	 * @return the box api key
	 */
	public ApiBoxKeyResponse getBoxApiKey();

	/**
	 * Gets the api key.
	 *
	 * @return the api key
	 */
	String getApiKey();

	/**
	 * Gets the folder.
	 *
	 * @param userBox the user box
	 * @param folderParentId the folder id
	 * @return the folder
	 */
	ItemResponse getFolder(UserBoxClient userBox, String folderParentId);

	/**
	 * Creates the folder.
	 *
	 * @param userBoxClient the user box client
	 * @param folderName the folder name
	 * @param folderParentId the folder parent id
	 * @return the item
	 */
	public Item createFolder(UserBoxClient userBoxClient, String folderName,
			String folderParentId);

}
