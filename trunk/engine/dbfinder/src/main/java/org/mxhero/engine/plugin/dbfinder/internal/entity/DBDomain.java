package org.mxhero.engine.plugin.dbfinder.internal.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="domain",schema="mxhero")
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
public class DBDomain {
	
	@Id
	@Column(name="domain",length=100, nullable=false)
	private String domain;
	
	@OneToMany(mappedBy="domain", cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	private Set<DBDomainAlias> aliases;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Set<DBDomainAlias> getAliases() {
		return aliases;
	}

	public void setAliases(Set<DBDomainAlias> aliases) {
		this.aliases = aliases;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
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
		DBDomain other = (DBDomain) obj;
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
			return false;
		return true;
	}

}
