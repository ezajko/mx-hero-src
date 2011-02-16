package org.mxhero.engine.plugin.featuresfp.internal.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.mxhero.engine.domain.feature.RuleProperty;

@Entity
@Table(name="features_rules_properties")
public class JpaRuleProperty implements RuleProperty{
	
	@Id
	private Integer id;
	
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name="rule_id")
	private JpaRule rule;
	
	@Column(name="property_key", nullable=false, length=50)
	private String propertyKey;
	
	@Column(name="property_value", nullable=false, length=200)
	private String propertyValue;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public JpaRule getRule() {
		return rule;
	}

	public void setRule(JpaRule rule) {
		this.rule = rule;
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((propertyKey == null) ? 0 : propertyKey.hashCode());
		result = prime * result + ((rule == null) ? 0 : rule.hashCode());
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
		JpaRuleProperty other = (JpaRuleProperty) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
