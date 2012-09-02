package org.mxhero.engine.plugin.boxstorage.internal.client.service;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.mxhero.engine.plugin.attachmentlink.alcommand.service.TransactionAttachment;
import org.mxhero.engine.plugin.boxstorage.internal.client.FileStored;
import org.mxhero.engine.plugin.boxstorage.internal.client.StorageResult;
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

	/** The request. */
	private UserRequest request;

	/** The file stored. */
	private FileUploadResponse fileStored;
	
	/** The url to retrieve token. */
	private String urlToRetrieveToken;
	
	/** The host mxhero server. */
	private String hostMxheroServer;
	
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
	 * @param tx the files path
	 * @return the storage result
	 */
	@Transactional
	public StorageResult store(TransactionAttachment tx) {
		logger.debug("Store file for user {}", getEmail());
		StorageResult result = new StorageResult(false);
		try {
			boolean hasBeenProccessed = persistence.hasBeenProccessed(tx.getFilePath());
			if(hasBeenProccessed){
				result.setAlreadyProccessed(true);
			}else{
				loadAccount(tx);
				setFileStored(connector.store(this, tx.getFilePath()));
				result = getFileStored().getResult();
				if(!result.isSuccess()){
					throw new RuntimeException("Could not store file. "+getFileStored().getResponse());
				}
				logger.debug("Sharing file to get public link to mxhero and Change file name");
				connector.updateFileInfo(this, tx.getOriginalFileName());
				if(getFileStored().couldCreateSharedLink()){
					Entry entry = this.getFileStored().getEntries().get(0);
					FileStored stored = new FileStored(entry.getShared_link().getUrl());
					result.setFileStored(stored);
				}else{
					logger.warn("File could be upload to user but could not be shared to be access in a public way");
				}
				persistence.registerStoreFile(this, tx.getFilePath());
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
	 * @param tx 
	 */
	public void loadAccount(TransactionAttachment tx) {
		logger.debug("Getting account token from storage");
		CreateTokenResponse accountFromStorage = persistence.getAccountFromStorage(getEmail());
		if(accountFromStorage == null){
			throw new RuntimeException("No account in storage with email "+getEmail());
		}
		setAccount(accountFromStorage);
		Item mxHeroFolder = createMxheroFolderIfNotExist();
		Item inboxFolder = createInboxFolderIfNotExist(mxHeroFolder);
		Item sentFolder = createSentFolderIfNotExist(mxHeroFolder);
		Item emailFolder = null;
		if(tx.isSender()){
			emailFolder = createEmailFolderIfNotExist(tx, sentFolder);
		}else if(tx.isRecipient()){
			emailFolder = createEmailFolderIfNotExist(tx, inboxFolder);
		}else{
			throw new RuntimeException("Could not identify sender or recipient to create folder");
		}
		this.getAccount().setItem(emailFolder);
	}

	/**
	 * Creates the email folder if not exist.
	 * @param inboxFolder 
	 *
	 * @return the item
	 */
	private Item createEmailFolderIfNotExist(TransactionAttachment tx, Item parentFolder) {
		return createFolderIfNotExist(tx.getFolderName(), parentFolder.getId());
	}

	/**
	 * Creates the sent folder if not exist.
	 * @param mxHeroFolder 
	 *
	 * @return the item
	 */
	private Item createSentFolderIfNotExist(Item mxHeroFolder) {
		return createFolderIfNotExist("Sent", mxHeroFolder.getId());
	}

	/**
	 * Creates the inbox folder if not exist.
	 * @param mxHeroFolder 
	 *
	 * @return the item
	 */
	private Item createInboxFolderIfNotExist(Item mxHeroFolder) {
		return createFolderIfNotExist("Inbox", mxHeroFolder.getId());
	}

	/**
	 * Creates the mxhero folder if not exist.
	 *
	 * @return the item
	 */
	private Item createMxheroFolderIfNotExist() {
		return createFolderIfNotExist("mxHero", "0");
	}

	/**
	 * Creates the folder if not exist.
	 *
	 * @param folderName the folder name
	 * @param folderParentId the folder parent id
	 * @return the item
	 */
	public Item createFolderIfNotExist(String folderName, String folderParentId) {
		logger.debug("Getting folder {} to store remote file", folderName);
		ItemResponse folder = connector.getFolder(this, folderParentId);
		Item itemFolder = folder.getFolder(folderName);
		if(itemFolder==null){
			logger.debug("{} folder is not present in user box. Creating {} folder", folderName, folderName);
			itemFolder = connector.createFolder(this, folderName, folderParentId);
		}
		if(StringUtils.isEmpty(itemFolder.getId())){
			throw new RuntimeException("Could not store file because folder "+ folderName +" doesnt not exist and could not be created");
		}
		return itemFolder;
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
			result.setBody(getUrlBody());
		}else{
			logger.debug("Store token to be use in synchronizer");
			storeToken();
		}
		return result;
	}

	/**
	 * Gets the url body.
	 *
	 * @return the url body
	 */
	private String getUrlBody() {
		String bodyUrl = "%s/api/init/auth?resp_url=%s&amp;auth_key=%s";
		return String.format(bodyUrl, getHostMxheroServer(), getUrlToRetrieveToken(), ApplicationKey.getKey());
	}

	/**
	 * Creates the account if not exsist.
	 */
	private void createAccountIfNotExsist() {
		CreateTokenResponse createAccount = connector.createAccount(getEmail());
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

	/**
	 * Gets the url to retrieve token.
	 *
	 * @return the url to retrieve token
	 */
	public String getUrlToRetrieveToken() {
		return urlToRetrieveToken;
	}

	/**
	 * Sets the url to retrieve token.
	 *
	 * @param urlToRetrieveToken the new url to retrieve token
	 */
	public void setUrlToRetrieveToken(String urlToRetrieveToken) {
		this.urlToRetrieveToken = urlToRetrieveToken;
	}

	/**
	 * Gets the host mxhero server.
	 *
	 * @return the host mxhero server
	 */
	public String getHostMxheroServer() {
		return hostMxheroServer;
	}

	/**
	 * Sets the host mxhero server.
	 *
	 * @param hostMxheroServer the new host mxhero server
	 */
	public void setHostMxheroServer(String hostMxheroServer) {
		this.hostMxheroServer = hostMxheroServer;
	}
}
