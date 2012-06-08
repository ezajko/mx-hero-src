package org.mxhero.webapi.vo;

import java.util.Calendar;
import java.util.List;

public class RuleVO {

	private Long id;
	
	private String domain;
	
	private String name;
	
	private Calendar created;
	
	private Calendar updated;
	
	private Boolean enabled;
	
	private Boolean twoWays;
	
	private String adminOrder;
	
	private String component;

	private RuleDirectionVO fromDirection;
	
	private RuleDirectionVO toDirection;
	
	private List<RulePropertyVO> properties;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public List<RulePropertyVO> getProperties() {
		return properties;
	}

	public void setProperties(List<RulePropertyVO> properties) {
		this.properties = properties;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
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
