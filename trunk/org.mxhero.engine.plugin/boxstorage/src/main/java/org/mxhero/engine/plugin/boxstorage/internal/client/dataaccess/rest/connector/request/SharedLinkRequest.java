package org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.request;

import java.io.Serializable;

/**
 * The Class SharedLinkRequest.
 */
public class SharedLinkRequest implements Serializable{
	
	/** The access. */
	private String access;
	
	/** The permissions. */
	private Permission permissions;
	
	public SharedLinkRequest() {
		this.access = "open";
		this.permissions = new Permission();
	}
	
	/**
	 * Gets the access.
	 *
	 * @return the access
	 */
	public String getAccess() {
		return access;
	}
	
	/**
	 * Gets the permissions.
	 *
	 * @return the permissions
	 */
	public Permission getPermissions() {
		return permissions;
	}
	
}
