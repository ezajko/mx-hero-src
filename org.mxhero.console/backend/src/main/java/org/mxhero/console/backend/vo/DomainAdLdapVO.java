package org.mxhero.console.backend.vo;

import java.util.Calendar;

public class DomainAdLdapVO {
	
	public static final String MANUAL="manual";
	
	private String domainId;
	
	private String directoryType;
	
	private String addres;
	
	private String port;
	
	private Boolean sslFlag;
	
	private String user;
	
	private String password;
	
	private String filter;
	
	private String base;
	
	private Calendar nextUpdate;
	
	private Calendar lastUpdate;
	
	private String error;
	
	private Boolean overrideFlag;
	
	private String dnAuthenticate;
	
	private String accountProperties;

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getDirectoryType() {
		return directoryType;
	}

	public void setDirectoryType(String directoryType) {
		this.directoryType = directoryType;
	}

	public String getAddres() {
		return addres;
	}

	public void setAddres(String addres) {
		this.addres = addres;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public Boolean getSslFlag() {
		return sslFlag;
	}

	public void setSslFlag(Boolean sslFlag) {
		this.sslFlag = sslFlag;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public Calendar getNextUpdate() {
		return nextUpdate;
	}

	public void setNextUpdate(Calendar nextUpdate) {
		this.nextUpdate = nextUpdate;
	}

	public Calendar getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Calendar lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Boolean getOverrideFlag() {
		return overrideFlag;
	}

	public void setOverrideFlag(Boolean overrideFlag) {
		this.overrideFlag = overrideFlag;
	}

	public String getDnAuthenticate() {
		return dnAuthenticate;
	}

	public void setDnAuthenticate(String dnAuthenticate) {
		this.dnAuthenticate = dnAuthenticate;
	}

	public String getAccountProperties() {
		return accountProperties;
	}

	public void setAccountProperties(String accountProperties) {
		this.accountProperties = accountProperties;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((domainId == null) ? 0 : domainId.hashCode());
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
		DomainAdLdapVO other = (DomainAdLdapVO) obj;
		if (domainId == null) {
			if (other.domainId != null)
				return false;
		} else if (!domainId.equals(other.domainId))
			return false;
		return true;
	}
	
}
