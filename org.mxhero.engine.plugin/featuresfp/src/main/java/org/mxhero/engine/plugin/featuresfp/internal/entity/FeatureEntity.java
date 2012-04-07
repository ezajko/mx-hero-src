package org.mxhero.engine.plugin.featuresfp.internal.entity;

import java.util.Set;

import org.mxhero.engine.commons.feature.Feature;
import org.mxhero.engine.commons.feature.Rule;

/**
 * @author mmarmol
 *
 */
public class FeatureEntity implements Feature{

	private Integer id;
	private Integer basePriority;
	private Integer version;
	private String component;
	private Set<Rule> rules;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Override
	public Integer getBasePriority() {
		return basePriority;
	}
	
	public void setBasePriority(Integer basePriority) {
		this.basePriority = basePriority;
	}
	
	@Override
	public Integer getVersion() {
		return version;
	}
	
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@Override
	public String getComponent() {
		return component;
	}
	
	public void setComponent(String component) {
		this.component = component;
	}
	
	@Override
	public Set<Rule> getRules() {
		return rules;
	}
	
	public void setRules(Set<Rule> rules) {
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
		FeatureEntity other = (FeatureEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
