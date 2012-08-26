package org.mxhero.engine.plugin.boxserver.service;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.mxhero.engine.plugin.boxserver.dataaccess.persistence.StoragePersistence;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.UserAccountAccess;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.CodeResponse;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.CreateUserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class UserBox.
 */
public class UserBox{

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(UserBox.class);
	
	/** The email. */
	private String email;
	
	/** The connector. */
	private UserAccountAccess connector;
	
	/** The account. */
	private CreateUserResponse account;
	
	/** The persistence. */
	private StoragePersistence persistence;

	/** The created by us. */
	private boolean createdByUs;
	
	/** The application id. */
	private String applicationId;

	/**
	 * Instantiates a new user box.
	 */
	public UserBox() {
	}
	
	/**
	 * Creates the user.
	 *
	 * @param email the email
	 * @return the user box
	 */
	public static UserBox createUser(String email){
		UserBox user = new UserBox();
		user.setEmail(email);
		return user;
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
	 * Creates the account.
	 */
	@Transactional
	public void createAccount() {
		try {
			logger.debug("Greate account for {}", this);
			setAccount(connector.createAccount(getEmail()));
			if(!hasPreviousAccount() && responseOk()){
				setCreatedByUs(true);
				persistence.storeAuthToken(this);
			}
		} catch (Exception e) {
			logger.error("Exception Class {}", e.getClass());
			logger.error("Exception Message {}", e.getMessage());
		}
	}
	


	/**
	 * Response ok.
	 *
	 * @return true, if successful
	 */
	private boolean responseOk() {
		return getAccount().wasResponseOk();
	}

	/**
	 * Retrieve token.
	 */
	public void retrieveToken() {
		CreateUserResponse accountFromStorage = persistence.getAccountFromStorage(getEmail());
		if(accountFromStorage == null){
			accountFromStorage = new CreateUserResponse();
			accountFromStorage.setResponse(CodeResponse.BAD_PARAMETERS);
		}else{
			accountFromStorage.setResponse(CodeResponse.OK);
		}
		setAccount(accountFromStorage);
	}

	/**
	 * Gets the connector.
	 *
	 * @return the connector
	 */
	public UserAccountAccess getConnector() {
		return connector;
	}
	
	/**
	 * Sets the connector.
	 *
	 * @param connector the new connector
	 */
	public void setConnector(UserAccountAccess connector) {
		this.connector = connector;
	}
	
	/**
	 * Gets the account.
	 *
	 * @return the account
	 */
	public CreateUserResponse getAccount() {
		return account;
	}

	/**
	 * Sets the account.
	 *
	 * @param account the new account
	 */
	public void setAccount(CreateUserResponse account) {
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
	public StoragePersistence getPersistence() {
		return persistence;
	}

	/**
	 * Sets the persistence.
	 *
	 * @param persistence the new persistence
	 */
	public void setPersistence(StoragePersistence persistence) {
		this.persistence = persistence;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.attachmentlink.cloudstorage.external.User#getToken()
	 */
	public String getToken() {
		return getAccount().getToken();
	}
	
	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.attachmentlink.cloudstorage.external.User#wasCreatedOk()
	 */
	public boolean wasCreatedOk() {
		return getAccount().wasResponseOk();
	}
	
	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.attachmentlink.cloudstorage.external.User#getResponseMessage()
	 */
	public String getResponseMessage() {
		return getAccount().getResponse().getMessage();
	}

	/**
	 * Sets the created by us.
	 *
	 * @param createdByUs the new created by us
	 */
	public void setCreatedByUs(boolean createdByUs) {
		this.createdByUs = createdByUs;
	}
	
	/**
	 * Checks if is created by us.
	 *
	 * @return true, if is created by us
	 */
	public boolean isCreatedByUs() {
		return createdByUs;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
}
