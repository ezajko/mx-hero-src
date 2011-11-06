package org.mxhero.engine.commons.feature;

import java.util.Collection;

public class Rule {

	private Integer id;

	private RuleDirection fromDirection;

	private RuleDirection toDirection;

	private String adminOrder;
	
	private String domain;
	
	private Boolean enabled;

	private Boolean twoWays;
	
	private Collection<RuleProperty> properties;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public RuleDirection getFromDirection() {
		return fromDirection;
	}

	public void setFromDirection(RuleDirection fromDirection) {
		this.fromDirection = fromDirection;
	}

	public RuleDirection getToDirection() {
		return toDirection;
	}

	public void setToDirection(RuleDirection toDirection) {
		this.toDirection = toDirection;
	}

	public String getAdminOrder() {
		return adminOrder;
	}

	public void setAdminOrder(String adminOrder) {
		this.adminOrder = adminOrder;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getTwoWays() {
		return twoWays;
	}

	public void setTwoWays(Boolean twoWays) {
		this.twoWays = twoWays;
	}

	public Collection<RuleProperty> getProperties() {
		return properties;
	}

	public void setProperties(Collection<RuleProperty> properties) {
		this.properties = properties;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rule other = (Rule) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


}
