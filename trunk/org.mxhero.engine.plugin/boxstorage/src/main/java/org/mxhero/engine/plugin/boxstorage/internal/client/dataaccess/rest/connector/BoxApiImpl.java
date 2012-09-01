package org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.BoxApi;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.AbstractResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.CodeResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.Entry;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.FileUploadResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.Item;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.ItemResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.request.SharedLinkRequest;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response.ApiBoxKeyResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response.CreateKeyResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response.CreateTokenResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response.ResponseHandler;
import org.mxhero.engine.plugin.boxstorage.internal.client.service.ApplicationKey;
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
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(BoxApiImpl.class);
	
	/** The handler. */
	private Map<String, ResponseHandler<? extends AbstractResponse>> handler;
	
	/** The template. */
	private RestTemplate template;
	
	/** The host mxhero box server. */
	private String hostMxheroBoxServer;
	
	/** The upload files url. */
	private String uploadFilesUrl;
	
	/** The search folders url. */
	private String foldersUrl;
	
	/** The shared file url. */
	private String sharedFileUrl;

	/** The encryptor jar. */
	private StandardPBEStringEncryptor encryptorJar;
	
	/** The Constant ALGORITHM_JAR. */
	private static final String ALGORITHM_JAR = "PBEWithMD5AndDES";

	/** The Constant ENCRYPTOR_JAR_SEED. */
	private static final String ENCRYPTOR_JAR_SEED = "clou-Stor$geE#cryp12orJar";

	/** The Constant HEXADECIMAL. */
	private static final String HEXADECIMAL = "hexadecimal";

	
	public BoxApiImpl() {
		encryptorJar = new StandardPBEStringEncryptor();
		encryptorJar.setAlgorithm(ALGORITHM_JAR);
		encryptorJar.setPassword(ENCRYPTOR_JAR_SEED);
		encryptorJar.setStringOutputType(HEXADECIMAL);
	}
	

	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.BoxApi#getAppKey(java.lang.String)
	 */
	@Override
	public CreateKeyResponse getAppKey(String applicationId) {
		logger.debug("Get AppKey for app instance {}", applicationId);
		Map<String , String> postParameters = new HashMap<String, String>();
		postParameters.put("name", applicationId);
		ResponseEntity<CreateKeyResponse> userResponse = getTemplate().postForEntity(getApplicationKeyUrl(), postParameters, CreateKeyResponse.class);
		CreateKeyResponse response = (CreateKeyResponse) handler.get("keys").handleReponse(userResponse);
		logger.debug("Response AppKey from Box mxHero Service {}", userResponse);
		return response;
	}
	
	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.BoxApi#getBoxApiKey()
	 */
	@Override
	public ApiBoxKeyResponse getBoxApiKey() {
		logger.debug("Get Box Api key for app");
		MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<String, String>();
		requestHeaders.add("Authorization", getApiBoxAuthHeader());
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<Map<String, String>>(requestHeaders);
		ResponseEntity<ApiBoxKeyResponse> userResponse = getTemplate().exchange(getBoxApiKeyUrl(), HttpMethod.GET, requestEntity, ApiBoxKeyResponse.class);
		ApiBoxKeyResponse response = (ApiBoxKeyResponse) handler.get("keys").handleReponse(userResponse);
		logger.debug("Response Box Api from Box mxHero Service");
		return response;
	}
	
	/**
	 * Gets the api box auth header.
	 *
	 * @return the api box auth header
	 */
	private String getApiBoxAuthHeader() {
    	String fileName = this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
    	File file  = new File(fileName);
		String key = encryptorJar.encrypt(file.getName());
		return String.format("%s&amp;module_key=%s",getAuthorizationMxheroHeader(), key);
	}

	/**
	 * Gets the box api key url.
	 *
	 * @return the box api key url
	 */
	private String getBoxApiKeyUrl() {
		return getHostMxheroBoxServer()+"/api/securekey";
	}

	/**
	 * Creates the account.
	 *
	 * @param user the user
	 * @return the creates the user response
	 */
	public CreateTokenResponse createAccount(String user){
		logger.debug("Create account for user {}", user);
		Map<String , String> postParameters = new HashMap<String, String>();
		postParameters.put("email", user);
		MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<String, String>();
		requestHeaders.add("Authorization", getAuthorizationMxheroHeader());
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<Map<String, String>>(postParameters, requestHeaders);
		ResponseEntity<CreateTokenResponse> userResponse = getTemplate().postForEntity(getCreateTokenUrl(), requestEntity, CreateTokenResponse.class);
		logger.debug("Response createAccount from Box {}", userResponse);
		CreateTokenResponse response = (CreateTokenResponse) handler.get("keys").handleReponse(userResponse);
		return response;
	}

	/**
	 * Gets the authorization mxhero header.
	 *
	 * @return the authorization mxhero header
	 */
	private String getAuthorizationMxheroHeader() {
		return String.format("mxHeroApi app_key=%s",ApplicationKey.getKey());
	}

	/**
	 * Store.
	 *
	 * @param userBox the user box
	 * @param filePath the file path
	 * @return the storage result
	 */
	public FileUploadResponse store(UserBoxClient userBox, String filePath) {
		logger.debug("Upload files for user {}", userBox);
		MultiValueMap<String , Object> postParameters = new LinkedMultiValueMap<String, Object>();
		postParameters.add("filename1", new FileSystemResource(filePath));
		postParameters.add("folder_id", userBox.getAccount().getItem().getId());
		MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<String, String>();
		String authKey = getAuthorizationHeaderBox(userBox);
		requestHeaders.add("Authorization", authKey);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(postParameters, requestHeaders);
		ResponseEntity<FileUploadResponse> userResponse = getTemplate().postForEntity(getUploadFilesUrl(), requestEntity, FileUploadResponse.class);
		FileUploadResponse response = (FileUploadResponse) handler.get("store").handleReponse(userResponse);
		logger.debug("Response uploadFiles from Box {}", response);
		return response;
	}

	/**
	 * Gets the authorization header box.
	 *
	 * @param userBox the user box
	 * @return the authorization header box
	 */
	private String getAuthorizationHeaderBox(UserBoxClient userBox) {
		String authKey = String.format("BoxAuth api_key=%s&auth_token=%s",getApiKey(),userBox.getAccount().getToken());
		return authKey;
	}
	
	/**
	 * Inits the api box key.
	 * @return 
	 */
	public String getApiKey() {
		logger.debug("Getting box api key to interact with box");
		String apiKey = ApplicationKey.getBoxApiKey();
		if(StringUtils.isEmpty(apiKey)){
			logger.debug("Box api key is not cached. Requesting api key to server");
			ApiBoxKeyResponse boxApiKey = getBoxApiKey();
			if(!boxApiKey.wasResponseOk()){
				throw new RuntimeException("Server mxhero Box could not retrieve Box API.");
			}
			apiKey = boxApiKey.getApiKey();
			ApplicationKey.setBoxApiKey(apiKey);
		}
		return ApplicationKey.getBoxApiKey();
	}

	
	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.BoxApi#shareFile(org.mxhero.engine.plugin.boxstorage.internal.client.service.UserBoxClient)
	 */
	@Override
	public void shareFile(UserBoxClient userBoxClient) {
		logger.debug("Shared file for user {}", userBoxClient);
		Map<String , Object> postParameters = new HashMap<String, Object>();
		postParameters.put("shared_link", new SharedLinkRequest());
		MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<String, String>();
		requestHeaders.add("Authorization", getAuthorizationHeaderBox(userBoxClient));
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
		requestHeaders.add("Authorization", getAuthorizationHeaderBox(userBox));
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
		requestHeaders.add("Authorization", getAuthorizationHeaderBox(userBox));
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
		return getHostMxheroBoxServer()+"/api/secure/attach/token/create";
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

	/**
	 * Gets the application key url.
	 *
	 * @return the application key url
	 */
	public String getApplicationKeyUrl() {
		return getHostMxheroBoxServer()+"/api/attach/application/key/create";
	}

	/**
	 * Gets the shared file url.
	 *
	 * @return the shared file url
	 */
	public String getSharedFileUrl() {
		return sharedFileUrl;
	}

	/**
	 * Sets the shared file url.
	 *
	 * @param sharedFileUrl the new shared file url
	 */
	public void setSharedFileUrl(String sharedFileUrl) {
		this.sharedFileUrl = sharedFileUrl;
	}

	/**
	 * Gets the host mxhero box server.
	 *
	 * @return the host mxhero box server
	 */
	public String getHostMxheroBoxServer() {
		return hostMxheroBoxServer;
	}

	/**
	 * Sets the host mxhero box server.
	 *
	 * @param hostMxheroBoxServer the new host mxhero box server
	 */
	public void setHostMxheroBoxServer(String hostMxheroBoxServer) {
		this.hostMxheroBoxServer = hostMxheroBoxServer;
	}

}
