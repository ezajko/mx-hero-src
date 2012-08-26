package org.mxhero.engine.plugin.boxserver;

import org.apache.commons.lang.StringUtils;

/**
 * The Class Application.
 */
public class Application {

	/** The name. */
	private String name;
	
	/** The enabled. */
	private boolean enabled;
	
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
	
	/**
	 * Checks if is enabled.
	 *
	 * @return true, if is enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * Sets the enabled.
	 *
	 * @param enabled the new enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * Auth ok.
	 *
	 * @return true, if successful
	 */
	public boolean authOk() {
		return exists() && isEnabled();
	}

	/**
	 * Exists.
	 *
	 * @return true, if successful
	 */
	public boolean exists() {
		return !StringUtils.isEmpty(name);
	} 
	
}
