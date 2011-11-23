package org.mxhero.console.backend.vo;

import java.util.Calendar;
import java.util.Collection;

public class DomainVO {

	private String domain;

	private String server;

	private Calendar creationDate;
	
	private Calendar updatedDate;

	private OwnerVO owner;
	
	private Collection<String> aliases;
	
	private String userName;

	private DomainAdLdapVO adLdap;
	
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

	public Calendar getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Calendar updatedDate) {
		this.updatedDate = updatedDate;
	}

	public OwnerVO getOwner() {
		return owner;
	}

	public void setOwner(OwnerVO owner) {
		this.owner = owner;
	}

	public Collection<String> getAliases() {
		return aliases;
	}

	public void setAliases(Collection<String> aliases) {
		this.aliases = aliases;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public DomainAdLdapVO getAdLdap() {
		return adLdap;
	}

	public void setAdLdap(DomainAdLdapVO adLdap) {
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
		DomainVO other = (DomainVO) obj;
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
			return false;
		return true;
	}
	
}
