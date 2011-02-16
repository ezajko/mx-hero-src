package org.mxhero.engine.domain.feature;

import java.util.Set;

public interface Rule {

	public Integer getId();

	public RuleDirection getFromDirection();

	public RuleDirection getToDirection();

	public String getAdminOrder();

	public Set<? extends RuleProperty> getProperties();

}
