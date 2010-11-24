package org.mxhero.engine.plugin.dbfinder.internal.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="domain")
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
public class DbDomain {

	@Id
	private Integer Id;
	
	@Column(name="domain")
	private String domain;
	
	@OneToMany(mappedBy="domain",fetch=FetchType.EAGER)
	private Set<DbAlias> aliases;
		
	@OneToMany(mappedBy="domain",fetch=FetchType.EAGER)
	private Set<DbGroup> groups;
	
	@OneToMany(mappedBy="domain",fetch=FetchType.EAGER)
	private Set<DbUser> users;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return Id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		Id = id;
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * @return the aliases
	 */
	public Set<DbAlias> getAliases() {
		return aliases;
	}

	/**
	 * @param aliases the aliases to set
	 */
	public void setAliases(Set<DbAlias> aliases) {
		this.aliases = aliases;
	}

	/**
	 * @return the groups
	 */
	public Set<DbGroup> getGroups() {
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(Set<DbGroup> groups) {
		this.groups = groups;
	}

	/**
	 * @return the users
	 */
	public Set<DbUser> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(Set<DbUser> users) {
		this.users = users;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Id == null) ? 0 : Id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DbDomain other = (DbDomain) obj;
		if (Id == null) {
			if (other.Id != null)
				return false;
		} else if (!Id.equals(other.Id))
			return false;
		return true;
	}

}
