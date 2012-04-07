package org.mxhero.engine.commons.rules.provider;

import java.util.Map;
import java.util.Set;

import org.mxhero.engine.commons.rules.CoreRule;

/**
 * 
 * @author mmarmol
 */
public interface RulesProvider {

	public Map<String, Set<CoreRule>> getRules();
	
}
