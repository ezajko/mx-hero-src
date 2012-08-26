package org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector;

import java.util.HashMap;
import java.util.Map;

import org.mxhero.engine.plugin.boxserver.dataaccess.rest.UserAccountAccess;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.AbstractResponse;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.CreateUserResponse;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.Item;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.ItemResponse;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.response.ResponseHandler;
import org.mxhero.engine.plugin.boxserver.service.UserBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * The Class BoxConnector.
 */
public class BoxConnector implements UserAccountAccess{
	
	/** The template. */
	private RestTemplate template;
	
	/** The create token url. */
	private String createTokenUrl;
	
	/** The upload files url. */
	private String uploadFilesUrl;
	
	/** The api key. */
	private String apiKey;
	
	/** The search folders url. */
	private String foldersUrl;
	
	/** The handler. */
	private Map<String, ResponseHandler<? extends AbstractResponse>> handler;
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(BoxConnector.class);

	/**
	 * Creates the account.
	 *
	 * @param user the user
	 * @return the creates the user response
	 */
	public CreateUserResponse createAccount(String user){
		logger.debug("Create account for user {}", user);
		Map<String , String> postParameters = new HashMap<String, String>();
		postParameters.put("email", user);
		MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<String, String>();
		requestHeaders.add("Authorization", "BoxAuth api_key="+getApiKey());
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<Map<String, String>>(postParameters, requestHeaders);
		ResponseEntity<CreateUserResponse> userResponse = getTemplate().postForEntity(getCreateTokenUrl(), requestEntity, CreateUserResponse.class);
		CreateUserResponse response = (CreateUserResponse) handler.get("createAccount").handleReponse(userResponse);
		logger.debug("Response createAccount from Box {}", response.getResponse());
		return response;
	}

	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.boxserver.internal.box.dataaccess.rest.UserAccountAccess#getFolderMxHero(org.mxhero.engine.plugin.boxserver.internal.box.service.UserBox)
	 */
	@Override
	public ItemResponse getFolderMxHero(UserBox userBox) {
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
	 * @see org.mxhero.engine.plugin.boxserver.internal.box.dataaccess.rest.UserAccountAccess#createMxHeroFolder(org.mxhero.engine.plugin.boxserver.internal.box.service.UserBox)
	 */
	@Override
	public Item createMxHeroFolder(UserBox userBox) {
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
	
}
