package org.mxhero.console.backend.vo;

import java.util.Calendar;
import java.util.Collection;

public class FeatureRuleVO {

	private Integer id;
	
	private String name;
	
	private Calendar created;
	
	private Calendar updated;
	
	private Boolean enabled;
	
	private String adminOrder;

	private FeatureRuleDirectionVO fromDirection;
	
	private FeatureRuleDirectionVO toDirection;
	
	private Collection<FeatureRulePropertyVO> properties;
	
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

	public String getAdminOrder() {
		return adminOrder;
	}

	public void setAdminOrder(String adminOrder) {
		this.adminOrder = adminOrder;
	}

	public FeatureRuleDirectionVO getFromDirection() {
		return fromDirection;
	}

	public void setFromDirection(FeatureRuleDirectionVO fromDirection) {
		this.fromDirection = fromDirection;
	}

	public FeatureRuleDirectionVO getToDirection() {
		return toDirection;
	}

	public void setToDirection(FeatureRuleDirectionVO toDirection) {
		this.toDirection = toDirection;
	}

	public Collection<FeatureRulePropertyVO> getProperties() {
		return properties;
	}

	public void setProperties(Collection<FeatureRulePropertyVO> properties) {
		this.properties = properties;
	}

}
