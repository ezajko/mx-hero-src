package org.mxhero.engine.plugin.featuresfp.internal.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.mxhero.engine.domain.feature.RuleDirection;

@Entity
@Table(name="features_rules_directions")
public class JpaRuleDirection implements RuleDirection {

	@Id
	private Integer id;

	@Column(name="directiom_type",length=100, nullable=false)
	private String directionType;
	
	@Column(name="free_value",length=100, nullable=false)
	private String freeValue;

	@OneToOne(fetch=FetchType.EAGER, cascade={CascadeType.MERGE})
	private JpaRule rule;
	
	@Column(name="domain",length=100)
	private String domain;
	
	@Column(name="group_name", length=100)
	private String group;
	
	@Column(name="account", length=100)
	private String account;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDirectionType() {
		return directionType;
	}

	public void setDirectionType(String directionType) {
		this.directionType = directionType;
	}

	public String getFreeValue() {
		return freeValue;
	}

	public void setFreeValue(String freeValue) {
		this.freeValue = freeValue;
	}

	public JpaRule getRule() {
		return rule;
	}

	public void setRule(JpaRule rule) {
		this.rule = rule;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		JpaRuleDirection other = (JpaRuleDirection) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("JpaRuleDirection [directionType=")
				.append(directionType).append(", freeValue=").append(freeValue)
				.append(", domain=").append(domain).append(", group=")
				.append(group).append(", account=").append(account).append("]");
		return builder.toString();
	}

}
