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

package org.mxhero.engine.commons.domain;

import java.util.Collection;
/**
 * Represents the Domain of a mail so it can be used in rules.
 * @author mmarmol
 *
 */
public class Domain {

	private String id;
	
	private Boolean managed;
	
	private Collection<String> aliases;

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return
	 */
	public Collection<String> getAliases() {
		return aliases;
	}

	/**
	 * @param aliases
	 */
	public void setAliases(Collection<String> aliases) {
		this.aliases = aliases;
	}

	/**
	 * @return
	 */
	public Boolean getManaged() {
		return managed;
	}

	/**
	 * @param managed
	 */
	public void setManaged(Boolean managed) {
		this.managed = managed;
	}

	/**
	 * @param alias
	 * @return
	 */
	public boolean hasAlias(String alias){
		if(this.getAliases()==null){
			return this.getId().equalsIgnoreCase(alias);
		}
		return this.getAliases().contains(alias.toLowerCase());
	}
	
	/**
	 * @param aliases
	 * @return
	 */
	public boolean hasAlias(String[] aliases){
		if(this.getAliases()==null){
			for(String alias : aliases){
				if(this.getId().equalsIgnoreCase(alias)){
					return true;
				}
			}
		}else{
			for(String alias : aliases){
				if(this.getAliases().contains(alias.toLowerCase())){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @param aliases
	 * @return
	 */
	public boolean hasAlias(Collection<String> aliases){
		if(this.getAliases()==null){
			for(String alias : aliases){
				if(this.getId().equalsIgnoreCase(alias)){
					return true;
				}
			}
		}else{
			for(String alias : aliases){
				if(this.getAliases().contains(alias.toLowerCase())){
					return true;
				}
			}
		}
		return false;
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
		Domain other = (Domain) obj;
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
		builder.append("Domain [id=").append(id).append(", managed=")
				.append(managed).append(", aliases=").append(aliases)
				.append("]");
		return builder.toString();
	}

}
