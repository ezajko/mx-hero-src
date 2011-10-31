package org.mxhero.engine.commons.rules.provider;

import java.util.Map;
import java.util.Set;

import org.mxhero.engine.commons.rules.CoreRule;

public interface RulesProvider {

	public Map<String, Set<CoreRule>> getRules();
	
}
