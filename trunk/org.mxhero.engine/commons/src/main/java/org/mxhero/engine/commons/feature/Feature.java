package org.mxhero.engine.commons.feature;

import java.util.Set;

public interface Feature {
	

	public Integer getBasePriority();

	public Integer getVersion();

	public String getComponent();

	public Set<Rule> getRules();

}
