package org.mxhero.console.backend.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AliasAccountPk implements Serializable{

	private static final long serialVersionUID = -835601432752220073L;

	@Column(name="account_alias", length=100)
	private String accountAlias;
	
	@Column(name="domain_alias", length=100, nullable=true)
	private String domainAlias;

	public AliasAccountPk(){
	}

	public AliasAccountPk(String accountAlias, String domainAlias) {
		super();
		this.accountAlias = accountAlias;
		this.domainAlias = domainAlias;
	}

	public String getAccountAlias() {
		return accountAlias;
	}

	public void setAccountAlias(String accountAlias) {
		this.accountAlias = accountAlias;
	}

	public String getDomainAlias() {
		return domainAlias;
	}

	public void setDomainAlias(String domainAlias) {
		this.domainAlias = domainAlias;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accountAlias == null) ? 0 : accountAlias.hashCode());
		result = prime * result
				+ ((domainAlias == null) ? 0 : domainAlias.hashCode());
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
		AliasAccountPk other = (AliasAccountPk) obj;
		if (accountAlias == null) {
			if (other.accountAlias != null)
				return false;
		} else if (!accountAlias.equals(other.accountAlias))
			return false;
		if (domainAlias == null) {
			if (other.domainAlias != null)
				return false;
		} else if (!domainAlias.equals(other.domainAlias))
			return false;
		return true;
	}

}
