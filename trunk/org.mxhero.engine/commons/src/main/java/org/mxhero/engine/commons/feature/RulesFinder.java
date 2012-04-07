package org.mxhero.engine.commons.feature;

/**
 * @author mmarmol
 *
 */
public interface RulesFinder {

	/**
	 * @param componet
	 * @param version
	 * @return
	 */
	Feature find(String componet, Integer version);
}
