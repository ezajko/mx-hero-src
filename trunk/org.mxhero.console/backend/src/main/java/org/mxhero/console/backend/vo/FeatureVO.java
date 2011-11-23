package org.mxhero.console.backend.vo;

import java.util.Collection;

public class FeatureVO {

	private Integer id;
	
	private String label;
	
	private String description;
	
	private String explain;
	
	private Collection<FeatureRuleVO> rules;

	private String moduleUrl;
	
	private String moduleReportUrl;
	
	private String component;
	
	private String defaultAdminOrder;
	
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

	public Collection<FeatureRuleVO> getRules() {
		return rules;
	}

	public void setRules(Collection<FeatureRuleVO> rules) {
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
		FeatureVO other = (FeatureVO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
