package org.mxhero.engine.plugin.boxserver.service;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.mxhero.engine.plugin.boxserver.Application;
import org.mxhero.engine.plugin.boxserver.dataaccess.persistence.StoragePersistence;

/**
 * The Class ApplicationBox.
 */
public class ApplicationBox {
	
	/** The Constant ALGORITHM_JAR. */
	private static final String ALGORITHM_JAR = "PBEWithMD5AndDES";

	/** The Constant ENCRYPTOR_JAR_SEED. */
	private static final String ENCRYPTOR_JAR_SEED = "clou-Stor$geE#cryp12orJar";
	
	/** The Constant HEXADECIMAL. */
	private static final String HEXADECIMAL = "hexadecimal";


	/** The persistence. */
	private StoragePersistence persistence;
	
	/** The encryptor. */
	private StandardPBEStringEncryptor encryptor;
	
	/** The api key. */
	private String apiBoxKey;

	private StandardPBEStringEncryptor encryptorJar;
	
	public ApplicationBox() {
		encryptorJar = new StandardPBEStringEncryptor();
		encryptorJar.setAlgorithm(ALGORITHM_JAR);
		encryptorJar.setPassword(ENCRYPTOR_JAR_SEED);
		encryptorJar.setStringOutputType(HEXADECIMAL);
	}

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

	/**
	 * Gets the api box key.
	 *
	 * @return the api box key
	 */
	public String getApiBoxKey() {
		return apiBoxKey;
	}
	
	/**
	 * Sets the api box key.
	 *
	 * @param apiBoxKey the new api box key
	 */
	public void setApiBoxKey(String apiBoxKey) {
		this.apiBoxKey = apiBoxKey;
	}

	/**
	 * Authenticate module.
	 *
	 * @param appKey the app key
	 * @return true, if successful
	 */
	public boolean authenticateModule(String appKey) {
		boolean auth = false;
		String moduleName = encryptorJar.decrypt(appKey);
		auth = persistence.authenticateModule(moduleName);
		return auth;
	}
	
}
