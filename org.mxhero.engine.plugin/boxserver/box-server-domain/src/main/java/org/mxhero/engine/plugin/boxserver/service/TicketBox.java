package org.mxhero.engine.plugin.boxserver.service;

import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.mxhero.engine.plugin.boxserver.dataaccess.persistence.StoragePersistence;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.AuthorizationAccess;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.CreateUserResponse;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.TicketResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class TicketBox.
 */
public class TicketBox {
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(TicketBox.class);
	
	/** The access. */
	private AuthorizationAccess access;
	
	/** The persistence. */
	private StoragePersistence persistence;
	
	/** The encryptor. */
	private PBEStringEncryptor encryptor;

	
	/**
	 * Gets the new ticket.
	 *
	 * @return the new ticket
	 */
	public String getNewTicket() {
		return getAccess().createNewTicket();
	}

	/**
	 * Register token.
	 *
	 * @param ticket the ticket
	 * @param token the token
	 * @param appKey the app key
	 * @return 
	 */
	public String registerToken(String ticket, String token, String appKey) {
		TicketResponse userToken = access.getUserToken(ticket);
		logger.debug("User was retrieve by Box {}", userToken);
		String appName = getEncryptor().decrypt(appKey);
		UserBox user = UserBox.createUser(userToken.getUser().getEmail());
		user.setAccount(new CreateUserResponse());
		user.getAccount().setToken(userToken.getAuth_token());
		user.setApplicationId(appName);
		persistence.storeAuthToken(user);
		logger.debug("User was persisted to upload files to Box in the future");
		return userToken.getUser().getEmail();
	}
	
	/**
	 * Gets the access.
	 *
	 * @return the access
	 */
	public AuthorizationAccess getAccess() {
		return access;
	}

	/**
	 * Sets the access.
	 *
	 * @param access the new access
	 */
	public void setAccess(AuthorizationAccess access) {
		this.access = access;
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

	/**
	 * Gets the encryptor.
	 *
	 * @return the encryptor
	 */
	public PBEStringEncryptor getEncryptor() {
		return encryptor;
	}

	/**
	 * Sets the encryptor.
	 *
	 * @param encryptor the new encryptor
	 */
	public void setEncryptor(PBEStringEncryptor encryptor) {
		this.encryptor = encryptor;
	}


}
