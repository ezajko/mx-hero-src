package org.mxhero.engine.plugin.adsync.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="domain",schema="mxhero")
public class Domain {
	
	@Id
	@Column(name="domain",length=100, nullable=false)
	private String domain;
	
	@OneToOne(mappedBy="domain", cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	private ApplicationUser owner;
	
	@OneToMany(mappedBy="domain", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Set<EmailAccount> emailAccounts;

	@OneToMany(mappedBy="domain", cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	private Set<DomainAlias> aliases;

	@OneToOne(mappedBy="domain",fetch=FetchType.EAGER,cascade=CascadeType.ALL, orphanRemoval=true) 
	private DomainAdLdap adLdap;
	
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public ApplicationUser getOwner() {
		return owner;
	}

	public void setOwner(ApplicationUser owner) {
		this.owner = owner;
	}

	public Set<EmailAccount> getEmailAccounts() {
		return emailAccounts;
	}

	public void setEmailAccounts(Set<EmailAccount> emailAccounts) {
		this.emailAccounts = emailAccounts;
	}
	
	public Set<DomainAlias> getAliases() {
		return aliases;
	}

	public void setAliases(Set<DomainAlias> aliases) {
		this.aliases = aliases;
	}
	
	public DomainAdLdap getAdLdap() {
		return adLdap;
	}

	public void setAdLdap(DomainAdLdap adLdap) {
		this.adLdap = adLdap;
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
		Domain other = (Domain) obj;
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
			return false;
		return true;
	}

}
