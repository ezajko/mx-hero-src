package org.mxhero.engine.commons.feature;

import java.util.Collection;

/**
 * @author mxhero
 *
 */
public interface Rule {

	/**
	 * @return
	 */
	public Integer getId();

	/**
	 * @return
	 */
	public RuleDirection getFromDirection();

	/**
	 * @return
	 */
	public RuleDirection getToDirection();

	/**
	 * @return
	 */
	public String getAdminOrder();

	/**
	 * @return
	 */
	public String getDomain();

	/**
	 * @return
	 */
	public Boolean getEnabled();

	/**
	 * @return
	 */
	public Boolean getTwoWays();

	/**
	 * @return
	 */
	public Collection<RuleProperty> getProperties();

}
