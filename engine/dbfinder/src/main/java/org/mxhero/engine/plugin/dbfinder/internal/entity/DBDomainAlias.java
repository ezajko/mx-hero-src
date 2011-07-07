package org.mxhero.engine.plugin.dbfinder.internal.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="domains_aliases",schema="mxhero")
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
public class DBDomainAlias {
	
	@Id
	@Column(name="alias", nullable=false, length=100)
	private String alias;
	
	@ManyToOne(cascade = {CascadeType.MERGE},fetch=FetchType.EAGER)
	@JoinColumn(name="domain")
	private DBDomain domain;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public DBDomain getDomain() {
		return domain;
	}

	public void setDomain(DBDomain domain) {
		this.domain = domain;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
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
		DBDomainAlias other = (DBDomainAlias) obj;
		if (alias == null) {
			if (other.alias != null)
				return false;
		} else if (!alias.equals(other.alias))
			return false;
		return true;
	}
	
}
