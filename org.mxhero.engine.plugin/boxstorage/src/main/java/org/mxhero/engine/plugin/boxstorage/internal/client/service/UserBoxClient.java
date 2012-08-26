package org.mxhero.engine.plugin.boxstorage.internal.client.service;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.persistence.ClientStoragePersistence;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.BoxApi;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.Entry;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.FileUploadResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.Item;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.ItemResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response.CreateTokenResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.domain.UserRequest;
import org.mxhero.engine.plugin.boxstorage.internal.client.service.strategy.AlreadyAccountStrategy;
import org.mxhero.engine.plugin.boxstorage.internal.client.service.strategy.AlreadyAccountStrategyFactory;
import org.mxhero.engine.plugin.storageapi.FileStored;
import org.mxhero.engine.plugin.storageapi.StorageResult;
import org.mxhero.engine.plugin.storageapi.UserResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class UserBoxClient.
 */
public class UserBoxClient{

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(UserBoxClient.class);
	
	/** The email. */
	private String email;
	
	/** The connector. */
	private BoxApi connector;
	
	/** The account. */
	private CreateTokenResponse account;
	
	/** The persistence. */
	private ClientStoragePersistence persistence;

	/** The application id. */
	private String applicationId;

	/** The request. */
	private UserRequest request;

	/** The body url. */
	private String bodyUrl;

	/** The file stored. */
	private FileUploadResponse fileStored;
	
	/**
	 * Instantiates a new user box.
	 */
	public UserBoxClient() {
	}
	
	/**
	 * Creates the user.
	 *
	 * @param email the email
	 * @return the user box
	 */
	public static UserBoxClient createUser(String email){
		UserBoxClient user = new UserBoxClient();
		user.setEmail(email);
		return user;
	}

	/**
	 * Creates the user.
	 *
	 * @param request the request
	 * @return the user box
	 */
	public static UserBoxClient createUser(UserRequest request){
		UserBoxClient user = new UserBoxClient();
		user.setRequest(request);
		user.setEmail(request.getEmail());
		return user;
	}
	
	/**
	 * Sets the user request.
	 *
	 * @param request the new user request
	 */
	public void setRequest(UserRequest request) {
		this.request = request;
	}
	
	/**
	 * Gets the request.
	 *
	 * @return the request
	 */
	public UserRequest getRequest() {
		return request;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	


	/**
	 * Store.
	 *
	 * @param filePath the files path
	 * @return the storage result
	 */
	@Transactional
	public StorageResult store(String filePath) {
		logger.debug("Store file for user {}", getEmail());
		StorageResult result = new StorageResult(false);
		try {
			boolean hasBeenProccessed = persistence.hasBeenProccessed(filePath);
			if(hasBeenProccessed){
				result.setAlreadyProccessed(true);
			}else{
				loadAccount();
				setFileStored(connector.store(this, filePath));
				result = getFileStored().getResult();
				if(!result.isSuccess()){
					throw new RuntimeException("Could not store file. "+getFileStored().getResponse());
				}
				logger.debug("Sharing file to get public link to mxhero");
				connector.shareFile(this);
				if(getFileStored().couldCreateSharedLink()){
					Entry entry = this.getFileStored().getEntries().get(0);
					FileStored stored = new FileStored(entry.getShared_link().getUrl());
					result.setFileStored(stored);
				}else{
					logger.warn("File could be upload to user but could not be shared to be access in a public way");
				}
				persistence.registerStoreFile(this, filePath);
			}
		} catch (Exception e) {
			logger.error("Exception Class {}", e.getClass());
			logger.error("Exception Message {}", e.getMessage());
			result.setMessage(e.getMessage());
		} 
		logger.debug("File fileStored successfully for user {}? {}", getEmail(), result.isSuccess());
		return result;
	}

	/**
	 * Load account.
	 */
	private void loadAccount() {
		logger.debug("Getting account token from storage");
		CreateTokenResponse accountFromStorage = persistence.getAccountFromStorage(getEmail());
		if(accountFromStorage == null){
			throw new RuntimeException("No account in storage with email "+getEmail());
		}
		setAccount(accountFromStorage);
		logger.debug("Getting folder mxhero to store remote file");
		ItemResponse folderMxHero = connector.getFolderMxHero(this);
		Item mxHeroFolder = folderMxHero.getMxHeroFolder();
		if(mxHeroFolder==null){
			logger.debug("Mxhero folder is not present in user box. Creating mxhero folder");
			mxHeroFolder = connector.createMxHeroFolder(this);
		}
		if(StringUtils.isEmpty(mxHeroFolder.getId())){
			throw new RuntimeException("Could not store file because folder mxhero doesnt not exist and could not be created");
		}
		this.getAccount().setItem(mxHeroFolder);
	}


	/**
	 * Sets the file stored.
	 *
	 * @param store the new file stored
	 */
	public void setFileStored(FileUploadResponse store) {
		this.fileStored = store;
	}
	
	/**
	 * Gets the file stored.
	 *
	 * @return the file stored
	 */
	public FileUploadResponse getFileStored() {
		return fileStored;
	}

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
	 * Gets the account.
	 *
	 * @return the account
	 */
	public CreateTokenResponse getAccount() {
		return account;
	}

	/**
	 * Sets the account.
	 *
	 * @param account the new account
	 */
	public void setAccount(CreateTokenResponse account) {
		this.account = account;
	}

	/**
	 * Checks for previous account.
	 *
	 * @return true, if successful
	 */
	public boolean hasPreviousAccount() {
		return getAccount().alreadyExist();
	}

	/**
	 * Gets the persistence.
	 *
	 * @return the persistence
	 */
	public ClientStoragePersistence getPersistence() {
		return persistence;
	}

	/**
	 * Sets the persistence.
	 *
	 * @param persistence the new persistence
	 */
	public void setPersistence(ClientStoragePersistence persistence) {
		this.persistence = persistence;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.storageapi.User#getToken()
	 */
	/**
	 * Gets the token.
	 *
	 * @return the token
	 */
	public String getToken() {
		return getAccount().getToken();
	}
	
	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.storageapi.User#wasCreatedOk()
	 */
	/**
	 * Was created ok.
	 *
	 * @return true, if successful
	 */
	public boolean wasCreatedOk() {
		return getAccount().wasResponseOk();
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
	 * Process.
	 *
	 * @return the result
	 */
	@Transactional
	public UserResult process() {
		logger.debug("Proccessing attachmentlink box for email {}", getEmail());
		UserResult result = new UserResult(getEmail());
		result.setSender(getRequest().isSender());
		createAccountIfNotExsist();
		if(hasPreviousAccount()){
			logger.debug("User has preivous account created in box. Proccessing to send it notification to external authentication in box");
			processAlreadyAccount(result);
			String bodyUrlNew = String.format(bodyUrl, ApplicationKey.getKey());
			result.setBody(bodyUrlNew);
		}else{
			logger.debug("Store token to be use in synchronizer");
			storeToken();
		}
		return result;
	}

	/**
	 * Creates the account if not exsist.
	 */
	private void createAccountIfNotExsist() {
		String appKey = ApplicationKey.getKey();
		if(StringUtils.isEmpty(appKey)){
			appKey = connector.getAppKey(getApplicationId());
			ApplicationKey.setKey(appKey);
		}
		CreateTokenResponse createAccount = connector.createAccount(getEmail(), appKey);
		setAccount(createAccount);
	}

	/**
	 * Store token.
	 */
	private void storeToken() {
		persistence.storeToken(this);
	}

	/**
	 * Process already account.
	 *
	 * @param result the result
	 */
	private void processAlreadyAccount(UserResult result) {
		AlreadyAccountStrategy strategy = AlreadyAccountStrategyFactory.create(getRequest());
		result.setAlreadyExist(true);
		strategy.execute(result);
	}
	
	/**
	 * Gets the body url.
	 *
	 * @return the body url
	 */
	public String getBodyUrl() {
		return bodyUrl;
	}
	
	/**
	 * Sets the body url.
	 *
	 * @param bodyUrl the new body url
	 */
	public void setBodyUrl(String bodyUrl) {
		this.bodyUrl = bodyUrl;
	}

	/**
	 * Register token.
	 *
	 * @param token the token
	 */
	public void registerToken(String token) {
		CreateTokenResponse tokenAccount = new CreateTokenResponse();
		tokenAccount.setEmail(getEmail());
		tokenAccount.setToken(token);
		setAccount(tokenAccount);
		persistence.storeToken(this);
		
	}
}
