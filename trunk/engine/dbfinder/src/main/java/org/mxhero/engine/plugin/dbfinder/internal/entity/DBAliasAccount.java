package org.mxhero.engine.plugin.dbfinder.internal.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="account_aliases",schema="mxhero")
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
public class DBAliasAccount {

	@EmbeddedId
	private DBAliasAccountPk id;

	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	@JoinColumns({
		@JoinColumn(name="account", referencedColumnName="account"),
		@JoinColumn(name="domain_id", referencedColumnName="domain_id")
	})
	private DBEmailAccount account;
	
	
	public DBAliasAccountPk getId() {
		return id;
	}

	public void setId(DBAliasAccountPk id) {
		this.id = id;
	}

	public DBEmailAccount getAccount() {
		return account;
	}

	public void setAccount(DBEmailAccount account) {
		this.account = account;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.getDomainAlias().hashCode());
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
		DBAliasAccount other = (DBAliasAccount) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	
}
