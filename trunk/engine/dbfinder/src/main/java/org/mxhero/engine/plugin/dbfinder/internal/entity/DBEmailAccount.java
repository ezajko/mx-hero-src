package org.mxhero.engine.plugin.dbfinder.internal.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="email_accounts",schema="mxhero")
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
public class DBEmailAccount {

	public static final String MANUAL="manual";
	
	@EmbeddedId
	private DBEmailAccountPk id;
	
	@MapsId("domainId")
	@ManyToOne(fetch=FetchType.EAGER,optional=false,cascade= {CascadeType.MERGE})
	@JoinColumns({
		@JoinColumn(name="domain_id", referencedColumnName="domain")
	})
	private DBDomain domain;

	@Column(nullable=true,name="group_name")
	private String group;
	
	@OneToMany(mappedBy="account", cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	private Set<DBAliasAccount> aliases;
	
	public DBEmailAccountPk getId() {
		return id;
	}

	public void setId(DBEmailAccountPk id) {
		this.id = id;
	}
	
	public Set<DBAliasAccount> getAliases() {
		return aliases;
	}

	public void setAliases(Set<DBAliasAccount> aliases) {
		this.aliases = aliases;
	}

	public DBDomain getDomain() {
		return domain;
	}

	public void setDomain(DBDomain domain) {
		this.domain = domain;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
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
		DBEmailAccount other = (DBEmailAccount) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
