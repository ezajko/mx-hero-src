package org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.request;

import java.io.Serializable;

/**
 * The Class Permission.
 */
public class Permission implements Serializable{
	
	/** The download. */
	private boolean download = true;
	
	/** The preview. */
	private boolean preview = true;
	
	/**
	 * Gets the download.
	 *
	 * @return the download
	 */
	public boolean getDownload() {
		return download;
	}
	
	/**
	 * Gets the preview.
	 *
	 * @return the preview
	 */
	public boolean getPreview() {
		return preview;
	}

}
