package org.mxhero.webapi.entity;

import java.util.Calendar;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "domain")
@XmlAccessorType(XmlAccessType.FIELD)
public class Domain {

	private String domain;
	private String server;
	private Calendar creationDate;
	private Calendar updatedDate;
	private ApplicationUser owner;
	private Connector domainSync;
	private Collection<String> aliases;
	
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

	public ApplicationUser getOwner() {
		return owner;
	}

	public void setOwner(ApplicationUser owner) {
		this.owner = owner;
	}

	public Connector getDomainSync() {
		return domainSync;
	}

	public void setDomainSync(Connector domainSync) {
		this.domainSync = domainSync;
	}

	public Collection<String> getAliases() {
		return aliases;
	}

	public void setAliases(Collection<String> aliases) {
		this.aliases = aliases;
	}
	
}
