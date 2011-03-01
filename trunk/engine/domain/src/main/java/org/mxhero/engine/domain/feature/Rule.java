package org.mxhero.engine.domain.feature;

import java.util.Collection;

public interface Rule {

	public Integer getId();

	public RuleDirection getFromDirection();

	public RuleDirection getToDirection();

	public String getAdminOrder();
	
	public String getDomain();

	public Collection<? extends RuleProperty> getProperties();

}
