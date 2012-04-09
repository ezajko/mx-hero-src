package org.mxhero.engine.commons.feature;

import java.util.Set;

/**
 * @author mmarmol
 *
 */
public interface Feature {
	
	/**
	 * @return
	 */
	public Integer getId();
	
	/**
	 * @return
	 */
	public Integer getBasePriority();

	/**
	 * @return
	 */
	public Integer getVersion();

	/**
	 * @return
	 */
	public String getComponent();

	/**
	 * @return
	 */
	public Set<Rule> getRules();

}
