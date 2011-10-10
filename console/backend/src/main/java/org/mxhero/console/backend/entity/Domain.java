package org.mxhero.console.backend.entity;

import java.util.Calendar;
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
	@Column(name="domain", nullable=false)
	private String domain;
	
	@Column(name="server", nullable=false)
	private String server;
	
	@Column(name="creation",nullable=false)
	private Calendar creationDate;

	@Column(name="updated",nullable=false)
	private Calendar updatesDate;
	
	@OneToOne(mappedBy="domain", cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	private ApplicationUser owner;
	
	@OneToMany(mappedBy="domain", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Set<EmailAccount> emailAccounts;

	@OneToMany(mappedBy="domain", cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	private Set<DomainAlias> aliases;
	
	@OneToMany(mappedBy="domain", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Set<Group> groups;
	
	@OneToMany(mappedBy="domain", cascade={CascadeType.REFRESH}, fetch=FetchType.EAGER)
	private Set<FeatureRule> rules;

	@OneToOne(mappedBy="domain",fetch=FetchType.EAGER,cascade=CascadeType.ALL, orphanRemoval=true) 
	private DomainAdLdap adLdap;
	
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public Calendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	public ApplicationUser getOwner() {
		return owner;
	}

	public void setOwner(ApplicationUser owner) {
		this.owner = owner;
	}

	public Calendar getUpdatesDate() {
		return updatesDate;
	}

	public void setUpdatesDate(Calendar updatesDate) {
		this.updatesDate = updatesDate;
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

	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	public Set<FeatureRule> getRules() {
		return rules;
	}

	public void setRules(Set<FeatureRule> rules) {
		this.rules = rules;
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