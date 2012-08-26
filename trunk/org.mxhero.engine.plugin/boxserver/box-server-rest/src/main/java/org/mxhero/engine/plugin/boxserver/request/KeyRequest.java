package org.mxhero.engine.plugin.boxserver.request;

import java.io.Serializable;

/**
 * The Class KeyRequest.
 */
public class KeyRequest implements Serializable{
	
	/** The name. */
	private String name;

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

}
