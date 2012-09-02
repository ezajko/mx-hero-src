package org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * The Class SharedLinkRequest.
 */
public class SharedLink implements Serializable{
	
	/** The url. */
	private String url;
	
	/** The download_url. */
	private String download_url;
	
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
	 * Gets the download_url.
	 *
	 * @return the download_url
	 */
	public String getDownload_url() {
		return download_url;
	}
	
	/**
	 * Sets the download_url.
	 *
	 * @param download_url the new download_url
	 */
	public void setDownload_url(String download_url) {
		this.download_url = download_url;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
