package org.mxhero.webapi.vo;

import java.util.Calendar;
import java.util.Collection;

public class RuleVO {

	private Integer id;
	
	private String domain;
	
	private String name;
	
	private Calendar created;
	
	private Calendar updated;
	
	private Boolean enabled;
	
	private Boolean twoWays;
	
	private String adminOrder;
	
	private Integer featureId;

	private RuleDirectionVO fromDirection;
	
	private RuleDirectionVO toDirection;
	
	private Collection<RulePropertyVO> properties;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Calendar getCreated() {
		return created;
	}

	public void setCreated(Calendar created) {
		this.created = created;
	}

	public Calendar getUpdated() {
		return updated;
	}

	public void setUpdated(Calendar updated) {
		this.updated = updated;
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

	public String getAdminOrder() {
		return adminOrder;
	}

	public void setAdminOrder(String adminOrder) {
		this.adminOrder = adminOrder;
	}

	public RuleDirectionVO getFromDirection() {
		return fromDirection;
	}

	public void setFromDirection(RuleDirectionVO fromDirection) {
		this.fromDirection = fromDirection;
	}

	public RuleDirectionVO getToDirection() {
		return toDirection;
	}

	public void setToDirection(RuleDirectionVO toDirection) {
		this.toDirection = toDirection;
	}

	public Collection<RulePropertyVO> getProperties() {
		return properties;
	}

	public void setProperties(Collection<RulePropertyVO> properties) {
		this.properties = properties;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public Integer getFeatureId() {
		return featureId;
	}

	public void setFeatureId(Integer featureId) {
		this.featureId = featureId;
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
		RuleVO other = (RuleVO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
