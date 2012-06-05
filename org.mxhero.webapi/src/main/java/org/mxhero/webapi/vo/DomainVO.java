package org.mxhero.webapi.vo;

import java.util.Calendar;
import java.util.List;

public class DomainVO {

	private String domain;
	private String server;
	private Calendar creationDate;
	private Calendar updatedDate;
	private List<String> aliases;
	private LdapVO ldap;
	
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
	
	public List<String> getAliases() {
		return aliases;
	}
	
	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}
	
	public LdapVO getLdap() {
		return ldap;
	}
	
	public void setLdap(LdapVO ldap) {
		this.ldap = ldap;
	}
	
}
