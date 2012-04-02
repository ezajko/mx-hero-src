package org.mxhero.webapi.entity;

import java.util.Calendar;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="rule")
@XmlAccessorType(XmlAccessType.FIELD)
public class Rule {

	private Integer id;
	
	private String domain;
	
	private String name;
	
	private Calendar created;
	
	private Calendar updated;
	
	private Boolean enabled;
	
	private Boolean twoWays;
	
	private String adminOrder;
	
	private Integer featureId;

	private RuleDirection fromDirection;
	
	private RuleDirection toDirection;
	
	@XmlAnyElement(lax=true)
	@XmlElementWrapper(name="properties")
	private List<RuleProperty> properties;
	
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

	public List<RuleProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<RuleProperty> properties) {
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

}
