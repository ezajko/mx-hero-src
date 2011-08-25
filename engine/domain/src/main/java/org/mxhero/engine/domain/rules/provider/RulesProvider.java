package org.mxhero.engine.domain.rules.provider;

import java.util.Map;
import java.util.Set;

import org.mxhero.engine.domain.rules.CoreRule;

public interface RulesProvider {

	public Map<String, Set<CoreRule>> getRules();
	
}
