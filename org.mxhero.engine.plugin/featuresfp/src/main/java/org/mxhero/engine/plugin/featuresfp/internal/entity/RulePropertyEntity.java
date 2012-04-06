package org.mxhero.engine.plugin.featuresfp.internal.entity;

import org.mxhero.engine.commons.feature.RuleProperty;

public class RulePropertyEntity implements RuleProperty {

	private String key;
	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
