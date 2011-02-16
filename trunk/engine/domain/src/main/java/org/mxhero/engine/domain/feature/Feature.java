package org.mxhero.engine.domain.feature;

import java.util.Set;

public interface Feature {

	public Integer getBasePriority();

	public Integer getVersion();
	
	public String getComponent();

	public Set<? extends Rule> getRules();

}
