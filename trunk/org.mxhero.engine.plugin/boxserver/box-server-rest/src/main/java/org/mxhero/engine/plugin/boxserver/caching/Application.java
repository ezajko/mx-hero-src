package org.mxhero.engine.plugin.boxserver.caching;

/**
 * The Class Application.
 */
public class Application {
	
	/** The url. */
	private String url;
	
	/** The app key. */
	private String appKey;
	
	/**
	 * Instantiates a new application.
	 *
	 * @param url the url
	 * @param appKey the app key
	 */
	public Application(String url, String appKey) {
		this.url = url;
		this.appKey = appKey;
	}
	
	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * Sets the url.
	 *
	 * @param url the new url
	 */
	public void setUrl(String url) {
		this.url = url;
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

}
