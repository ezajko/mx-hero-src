/*
 * 
 */
package org.mxhero.engine.plugin.boxstorage.internal.client;

/**
 * The Class FileStored.
 */
public class FileStored {

	/** The url. */
	private String url;
	
	/**
	 * Instantiates a new file stored.
	 *
	 * @param id the id
	 * @param url the url
	 */
	public FileStored(String url) {
		this.url = url;
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

}
