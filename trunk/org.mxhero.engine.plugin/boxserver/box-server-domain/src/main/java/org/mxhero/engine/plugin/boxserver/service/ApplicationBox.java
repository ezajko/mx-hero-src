package org.mxhero.engine.plugin.boxserver.service;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.mxhero.engine.plugin.boxserver.Application;
import org.mxhero.engine.plugin.boxserver.dataaccess.persistence.StoragePersistence;

/**
 * The Class ApplicationBox.
 */
public class ApplicationBox {
	
	/** The persistence. */
	private StoragePersistence persistence;
	
	/** The encryptor. */
	private StandardPBEStringEncryptor encryptor;

	/**
	 * Authenticate.
	 *
	 * @param appKey the app key
	 * @return true, if successful
	 */
	public boolean authenticate(String appKey) {
		String decrypt = getEncryptor().decrypt(appKey);
		Application app = getPersistence().getAppFromKey(decrypt);
		return app.authOk();
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
	public StandardPBEStringEncryptor getEncryptor() {
		return encryptor;
	}

	/**
	 * Sets the encryptor.
	 *
	 * @param encryptor the new encryptor
	 */
	public void setEncryptor(StandardPBEStringEncryptor encryptor) {
		this.encryptor = encryptor;
	}

	/**
	 * Gets the app key.
	 *
	 * @param name the name
	 * @return the app key
	 */
	public String getAppKey(String name) {
		Application appFromKey = persistence.getAppFromKey(name);
		if(!appFromKey.exists()){
			persistence.registerNewApp(name);
		}
		return encryptor.encrypt(name);
	}
	
	
}
