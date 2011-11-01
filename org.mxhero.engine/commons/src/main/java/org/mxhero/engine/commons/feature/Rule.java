package org.mxhero.engine.commons.feature;

import java.util.Collection;

public interface Rule {

	public Integer getId();

	public RuleDirection getFromDirection();

	public RuleDirection getToDirection();

	public String getAdminOrder();
	
	public String getDomain();
	
	public Boolean getEnabled();

	public Boolean getTwoWays();
	
	public Collection<? extends RuleProperty> getProperties();

}
