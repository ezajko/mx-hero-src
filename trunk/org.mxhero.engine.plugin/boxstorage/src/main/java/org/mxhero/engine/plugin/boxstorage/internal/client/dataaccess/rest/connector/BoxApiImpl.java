package org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector;

import java.util.HashMap;
import java.util.Map;

import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.BoxApi;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.AbstractResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.CodeResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.Entry;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.FileUploadResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.Item;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.ItemResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.request.SharedLinkRequest;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response.CreateKeyResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response.CreateTokenResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response.ResponseHandler;
import org.mxhero.engine.plugin.boxstorage.internal.client.service.UserBoxClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * The Class BoxApiImpl.
 */
public class BoxApiImpl implements BoxApi{
	
	/** The template. */
	private RestTemplate template;
	
	/** The create token url. */
	private String createTokenUrl;
	
	/** The upload files url. */
	private String uploadFilesUrl;
	
	/** The application key url. */
	private String applicationKeyUrl;
	
	/** The api key. */
	private String apiKey;
	
	/** The search folders url. */
	private String foldersUrl;
	
	/** The shared file url. */
	private String sharedFileUrl;
	
	/** The handler. */
	private Map<String, ResponseHandler<? extends AbstractResponse>> handler;
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(BoxApiImpl.class);

	@Override
	public String getAppKey(String applicationId) {
		logger.debug("Get AppKey for app instance {}", applicationId);
		Map<String , String> postParameters = new HashMap<String, String>();
		postParameters.put("name", applicationId);
		CreateKeyResponse userResponse = getTemplate().postForObject(getApplicationKeyUrl(), postParameters, CreateKeyResponse.class);
		logger.debug("Response AppKey from Box mxHero Service {}", userResponse);
		return userResponse.getAppKey();
	}
	
	/**
	 * Creates the account.
	 *
	 * @param user the user
	 * @return the creates the user response
	 */
	public CreateTokenResponse createAccount(String user, String appKey){
		logger.debug("Create account for user {}", user);
		Map<String , String> postParameters = new HashMap<String, String>();
		postParameters.put("email", user);
		MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<String, String>();
		requestHeaders.add("Authorization", "mxHeroApi app_key="+appKey);
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<Map<String, String>>(postParameters, requestHeaders);
		CreateTokenResponse userResponse = getTemplate().postForObject(getCreateTokenUrl(), requestEntity, CreateTokenResponse.class);
		logger.debug("Response createAccount from Box {}", userResponse);
		return userResponse;
	}

	/**
	 * Store.
	 *
	 * @param userBox the user box
	 * @param filesPath the files path
	 * @return the storage result
	 */
	public FileUploadResponse store(UserBoxClient userBox, String filePath) {
		logger.debug("Upload files for user {}", userBox);
		MultiValueMap<String , Object> postParameters = new LinkedMultiValueMap<String, Object>();
		postParameters.add("filename1", new FileSystemResource(filePath));
		postParameters.add("folder_id", userBox.getAccount().getItem().getId());
		MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<String, String>();
		requestHeaders.add("Authorization", "BoxAuth api_key="+getApiKey()+"&auth_token="+userBox.getAccount().getToken());
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(postParameters, requestHeaders);
		ResponseEntity<FileUploadResponse> userResponse = getTemplate().postForEntity(getUploadFilesUrl(), requestEntity, FileUploadResponse.class);
		FileUploadResponse response = (FileUploadResponse) handler.get("store").handleReponse(userResponse);
		logger.debug("Response uploadFiles from Box {}", response);
		return response;
	}
	
	@Override
	public void shareFile(UserBoxClient userBoxClient) {
		logger.debug("Shared file for user {}", userBoxClient);
		Map<String , Object> postParameters = new HashMap<String, Object>();
		postParameters.put("shared_link", new SharedLinkRequest());
		MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<String, String>();
		requestHeaders.add("Authorization", "BoxAuth api_key="+getApiKey()+"&auth_token="+userBoxClient.getAccount().getToken());
		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(postParameters, requestHeaders);
		String url = getSharedFileUrl()+"/"+userBoxClient.getFileStored().getEntries().get(0).getId();
		ResponseEntity<Entry> userResponse = getTemplate().exchange(url, HttpMethod.PUT, requestEntity, Entry.class);
		if(userResponse.getStatusCode().value()<300){
			userBoxClient.getFileStored().setResponseSharedLink(CodeResponse.OK);
			userBoxClient.getFileStored().getEntries().clear();
			userBoxClient.getFileStored().getEntries().add(userResponse.getBody());
		}else{
			userBoxClient.getFileStored().setResponseSharedLink(CodeResponse.SHARED_LINK_COULD_NOT_BE_CREATED);
		}
		logger.debug("Response uploadFiles from Box {}", userResponse);
	}
	
	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.boxstorage.internal.service.internal.box.dataaccess.rest.UserAccountAccess#getFolderMxHero(org.mxhero.engine.plugin.boxstorage.internal.service.internal.box.service.UserBox)
	 */
	@Override
	public ItemResponse getFolderMxHero(UserBoxClient userBox) {
		logger.debug("Getting folder item mxhero account for user {}", userBox);
		MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<String, String>();
		requestHeaders.add("Authorization", "BoxAuth api_key="+getApiKey()+"&auth_token="+userBox.getAccount().getToken());
		HttpEntity requestEntity = new HttpEntity(requestHeaders);
		ResponseEntity<ItemResponse> itemResposne = getTemplate().exchange(getFoldersUrl(), HttpMethod.GET, requestEntity, ItemResponse.class);
		ItemResponse response = (ItemResponse) handler.get("createAccount").handleReponse(itemResposne);
		logger.debug("Response getFolderMxHero from Box {}", response.getResponse());
		return response;
	}
	
	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.boxstorage.internal.service.internal.box.dataaccess.rest.UserAccountAccess#createMxHeroFolder(org.mxhero.engine.plugin.boxstorage.internal.service.internal.box.service.UserBox)
	 */
	@Override
	public Item createMxHeroFolder(UserBoxClient userBox) {
		logger.debug("Create folder mxHero for account user {}", userBox);
		Map<String , String> postParameters = new HashMap<String, String>();
		postParameters.put("name", "mxHero");
		MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<String, String>();
		requestHeaders.add("Authorization", "BoxAuth api_key="+getApiKey()+"&auth_token="+userBox.getAccount().getToken());
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<Map<String, String>>(postParameters, requestHeaders);
		ResponseEntity<Item> userResponse = getTemplate().postForEntity(getFoldersUrl(), requestEntity, Item.class);
		logger.debug("Folder mxHero created from Box account {}", userBox);
		return userResponse.getBody();
	}

	/**
	 * Gets the template.
	 *
	 * @return the template
	 */
	public RestTemplate getTemplate() {
		return template;
	}


	/**
	 * Sets the template.
	 *
	 * @param template the new template
	 */
	public void setTemplate(RestTemplate template) {
		this.template = template;
	}


	/**
	 * Gets the creates the token url.
	 *
	 * @return the creates the token url
	 */
	public String getCreateTokenUrl() {
		return createTokenUrl;
	}


	/**
	 * Sets the creates the token url.
	 *
	 * @param createTokenUrl the new creates the token url
	 */
	public void setCreateTokenUrl(String createTokenUrl) {
		this.createTokenUrl = createTokenUrl;
	}


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

	/**
	 * Gets the upload files url.
	 *
	 * @return the upload files url
	 */
	public String getUploadFilesUrl() {
		return uploadFilesUrl;
	}

	/**
	 * Sets the upload files url.
	 *
	 * @param uploadFilesUrl the new upload files url
	 */
	public void setUploadFilesUrl(String uploadFilesUrl) {
		this.uploadFilesUrl = uploadFilesUrl;
	}

	/**
	 * Gets the handler.
	 *
	 * @return the handler
	 */
	public Map<String, ResponseHandler<? extends AbstractResponse>> getHandler() {
		return handler;
	}

	/**
	 * Sets the handler.
	 *
	 * @param handler the handler
	 */
	public void setHandler(Map<String, ResponseHandler<? extends AbstractResponse>> handler) {
		this.handler = handler;
	}

	
	/**
	 * Gets the folders url.
	 *
	 * @return the folders url
	 */
	public String getFoldersUrl() {
		return foldersUrl;
	}
	
	/**
	 * Sets the folders url.
	 *
	 * @param foldersUrl the new folders url
	 */
	public void setFoldersUrl(String foldersUrl) {
		this.foldersUrl = foldersUrl;
	}

	public String getApplicationKeyUrl() {
		return applicationKeyUrl;
	}

	public void setApplicationKeyUrl(String applicationKeyUrl) {
		this.applicationKeyUrl = applicationKeyUrl;
	}

	public String getSharedFileUrl() {
		return sharedFileUrl;
	}

	public void setSharedFileUrl(String sharedFileUrl) {
		this.sharedFileUrl = sharedFileUrl;
	}

}
