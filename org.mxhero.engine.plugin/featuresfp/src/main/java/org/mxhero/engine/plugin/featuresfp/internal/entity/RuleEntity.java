/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mxhero.engine.plugin.featuresfp.internal.entity;

import java.util.Collection;

import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;

/**
 * @author mmarmol
 *
 */
public class RuleEntity implements Rule{

	private Integer id;
	private RuleDirectionEntity fromDirection;
	private RuleDirectionEntity toDirection;
	private String adminOrder;
	private String domain;
	private Boolean enabled;
	private Boolean twoWays;
	private Collection<RuleProperty> properties;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public RuleDirectionEntity getFromDirection() {
		return fromDirection;
	}
	
	public void setFromDirection(RuleDirectionEntity fromDirection) {
		this.fromDirection = fromDirection;
	}
	
	public RuleDirectionEntity getToDirection() {
		return toDirection;
	}
	
	public void setToDirection(RuleDirectionEntity toDirection) {
		this.toDirection = toDirection;
	}
	
	public String getAdminOrder() {
		return adminOrder;
	}
	
	public void setAdminOrder(String adminOrder) {
		this.adminOrder = adminOrder;
	}
	
	public String getDomain() {
		return domain;
	}
	
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public Boolean getTwoWays() {
		return twoWays;
	}
	
	public void setTwoWays(Boolean twoWays) {
		this.twoWays = twoWays;
	}
	
	public Collection<RuleProperty> getProperties() {
		return properties;
	}
	
	public void setProperties(Collection<RuleProperty> properties) {
		this.properties = properties;
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
		RuleEntity other = (RuleEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
