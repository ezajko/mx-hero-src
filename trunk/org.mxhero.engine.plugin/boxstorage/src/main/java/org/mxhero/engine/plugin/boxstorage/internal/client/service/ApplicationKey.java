package org.mxhero.engine.plugin.boxstorage.internal.client.service;

/**
 * The Class ApplicationKey.
 */
public class ApplicationKey {
	
	/** The app key. */
	private String appKey;
	
	/** The box key. */
	private String boxKey;
	
	/** The instance. */
	private static ApplicationKey instance;
	
	/**
	 * Instantiates a new application key.
	 */
	private ApplicationKey(){}
	
	/**
	 * Sets the key.
	 *
	 * @param key the new key
	 */
	public static void setKey(String key){
		getInstance().setAppKey(key);
	}

	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public static String getKey(){
		return getInstance().getAppKey();
	}

	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public static String getBoxApiKey(){
		return getInstance().getBoxKey();
	}

	/**
	 * Sets the box api key.
	 *
	 * @param key the new box api key
	 */
	public static void setBoxApiKey(String key){
		getInstance().setBoxKey(key);
	}
	
	/**
	 * Gets the single instance of ApplicationKey.
	 *
	 * @return single instance of ApplicationKey
	 */
	private static ApplicationKey getInstance() {
		if(instance == null)instance = new ApplicationKey();
		return instance;
	}

	/**
	 * Gets the app key.
	 *
	 * @return the app key
	 */
	public String getAppKey() {
		return appKey;
	}
	
	/**
	 * Sets the app key.
	 *
	 * @param appKey the new app key
	 */
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	/**
	 * Gets the box key.
	 *
	 * @return the box key
	 */
	public String getBoxKey() {
		return boxKey;
	}

	/**
	 * Sets the box key.
	 *
	 * @param boxKey the new box key
	 */
	public void setBoxKey(String boxKey) {
		this.boxKey = boxKey;
	}


}
