package org.mxhero.webapi.entity;

import java.util.Collection;

public class Feature {

	private Integer id;
	
	private String label;
	
	private String description;
	
	private String explain;
	
	private Collection<Rule> rules;

	private String moduleUrl;
	
	private String moduleReportUrl;
	
	private String component;
	
	private String defaultAdminOrder;
	
	private Boolean enabled;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public Collection<Rule> getRules() {
		return rules;
	}

	public void setRules(Collection<Rule> rules) {
		this.rules = rules;
	}

	public String getModuleUrl() {
		return moduleUrl;
	}

	public void setModuleUrl(String moduleUrl) {
		this.moduleUrl = moduleUrl;
	}

	public String getModuleReportUrl() {
		return moduleReportUrl;
	}

	public void setModuleReportUrl(String moduleReportUrl) {
		this.moduleReportUrl = moduleReportUrl;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getDefaultAdminOrder() {
		return defaultAdminOrder;
	}

	public void setDefaultAdminOrder(String defaultAdminOrder) {
		this.defaultAdminOrder = defaultAdminOrder;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

}
