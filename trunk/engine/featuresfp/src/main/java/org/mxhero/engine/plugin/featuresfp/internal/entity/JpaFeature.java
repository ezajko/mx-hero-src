package org.mxhero.engine.plugin.featuresfp.internal.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.mxhero.engine.domain.feature.Feature;

@Entity
@Table(name="features")
public class JpaFeature implements Serializable, Feature {

	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	
	@Column(name="base_priority", nullable=false)
	private Integer basePriority;
	
	@Column(name="version", nullable=false)
	private Integer version;
	
	@Column(name="component", nullable=false, length=100)
	private String component;

	@OneToMany(mappedBy="feature", cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	private Set<JpaRule> rules;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBasePriority() {
		return basePriority;
	}

	public void setBasePriority(Integer basePriority) {
		this.basePriority = basePriority;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
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
		result = prime * result
				+ ((component == null) ? 0 : component.hashCode());
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
		JpaFeature other = (JpaFeature) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
