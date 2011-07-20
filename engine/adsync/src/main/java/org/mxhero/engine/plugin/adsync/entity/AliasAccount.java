package org.mxhero.engine.plugin.adsync.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name="account_aliases",schema="mxhero")
public class AliasAccount {

	@EmbeddedId
	private AliasAccountPk id;

	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	@JoinColumns({
		@JoinColumn(name="account", referencedColumnName="account"),
		@JoinColumn(name="domain_id", referencedColumnName="domain_id")
	})
	private EmailAccount account;
	
	@MapsId("domainAlias")
	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	@JoinColumns({
		@JoinColumn(name="domain_alias", referencedColumnName="alias")
	})
	private DomainAlias domainAlias;
	
	@Column(name="created", nullable=false)
	private Calendar created;

	@Column(name="data_source", nullable=false)
	private String dataSource;
	
	public AliasAccountPk getId() {
		return id;
	}

	public void setId(AliasAccountPk id) {
		this.id = id;
	}

	public EmailAccount getAccount() {
		return account;
	}

	public void setAccount(EmailAccount account) {
		this.account = account;
	}

	public DomainAlias getDomainAlias() {
		return domainAlias;
	}

	public void setDomainAlias(DomainAlias domainAlias) {
		this.domainAlias = domainAlias;
	}

	public Calendar getCreated() {
		return created;
	}

	public void setCreated(Calendar created) {
		this.created = created;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
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
		AliasAccount other = (AliasAccount) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	
}
