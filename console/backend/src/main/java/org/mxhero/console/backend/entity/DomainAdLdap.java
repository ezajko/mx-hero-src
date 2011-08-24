package org.mxhero.console.backend.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="domain_adldap",schema="mxhero")
public class DomainAdLdap {

	@Id
	@Column(name="domain_id")
	private String domainId;
	
	@MapsId
	@OneToOne
	@JoinColumn(name="domain")
	private Domain domain;
	
	@Column(name="directory_type", length=100)
	private String directoryType;
	
	@Column(name="address")
	private String addres;
	
	@Column(name="port", length=10)
	private String port;
	
	@Column(name="ssl_flag")
	private Boolean sslFlag;
	
	@Column(name="user")
	private String user;
	
	@Column(name="password")
	private String password;
	
	@Column(name="filter")
	private String filter;
	
	@Column(name="base")
	private String base;
	
	@Column(name="dn_authenticate")
	private String dnAuthenticate;
	
	@Column(name="next_update")
	private Calendar nextUpdate;
	
	@Column(name="last_update")
	private Calendar lastUpdate;
	
	@Column(name="last_error")
	private String error;
	
	@Column(name="override_flag")
	private Boolean overrideFlag;

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
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

	public Boolean getOverrideFlag() {
		return overrideFlag;
	}

	public void setOverrideFlag(Boolean overrideFlag) {
		this.overrideFlag = overrideFlag;
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

	public String getDomainId() {
		return domainId;
	}

	public String getDirectoryType() {
		return directoryType;
	}

	public void setDirectoryType(String directoryType) {
		this.directoryType = directoryType;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getDnAuthenticate() {
		return dnAuthenticate;
	}

	public void setDnAuthenticate(String dnAuthenticate) {
		this.dnAuthenticate = dnAuthenticate;
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
		DomainAdLdap other = (DomainAdLdap) obj;
		if (domainId == null) {
			if (other.domainId != null)
				return false;
		} else if (!domainId.equals(other.domainId))
			return false;
		return true;
	}

}
