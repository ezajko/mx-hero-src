package org.mxhero.console.backend.entity;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="domain")
public class Domain {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name="domain",length=100, unique=true, nullable=false)
	private String domain;
	
	@Column(name="server",length=100, nullable=false)
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
	
	@OneToMany(mappedBy="domain", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Set<FeatureRule> rules;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

}
