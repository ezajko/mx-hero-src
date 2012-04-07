package org.mxhero.engine.plugin.featuresfp.internal.entity;

import java.util.Collection;

import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;

/**
 * @author mmarmol
 *
 */
public class RuleEntity implements Rule{

	private Integer id;
	private RuleDirectionEntity fromDirection;
	private RuleDirectionEntity toDirection;
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
	
	public RuleDirectionEntity getFromDirection() {
		return fromDirection;
	}
	
	public void setFromDirection(RuleDirectionEntity fromDirection) {
		this.fromDirection = fromDirection;
	}
	
	public RuleDirectionEntity getToDirection() {
		return toDirection;
	}
	
	public void setToDirection(RuleDirectionEntity toDirection) {
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
		RuleEntity other = (RuleEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
