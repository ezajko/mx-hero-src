package org.mxhero.engine.plugin.featuresfp.internal.entity;

import org.mxhero.engine.commons.feature.RuleDirection;

public class RuleDirectionEntity implements RuleDirection {

	private Integer id;
	private String directionType;
	private String freeValue;
	private String group;
	private String account;
	private String domain;

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

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
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
		RuleDirectionEntity other = (RuleDirectionEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
