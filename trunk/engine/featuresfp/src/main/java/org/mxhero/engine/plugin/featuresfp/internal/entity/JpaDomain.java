package org.mxhero.engine.plugin.featuresfp.internal.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="domain")
public class JpaDomain {

	@Id
	private Integer id;
	
	@Column(name="domain", nullable=false, length=100)
	private String domain;

	@OneToMany(mappedBy="domainEntity", cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	private Set<JpaRule> rules;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Set<JpaRule> getRules() {
		return rules;
	}

	public void setRules(Set<JpaRule> rules) {
		this.rules = rules;
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
		JpaDomain other = (JpaDomain) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
