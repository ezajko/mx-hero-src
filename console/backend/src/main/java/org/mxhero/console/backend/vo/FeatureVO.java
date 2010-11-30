package org.mxhero.console.backend.vo;

import java.util.Set;

public class FeatureVO {

	private Integer id;
	
	private String label;
	
	private String description;
	
	private Set<FeatureRuleVO> rules;

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

	public Set<FeatureRuleVO> getRules() {
		return rules;
	}

	public void setRules(Set<FeatureRuleVO> rules) {
		this.rules = rules;
	}

}
