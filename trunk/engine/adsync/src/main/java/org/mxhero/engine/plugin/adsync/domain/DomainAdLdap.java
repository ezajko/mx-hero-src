package org.mxhero.engine.plugin.adsync.domain;

import java.util.Calendar;

public class DomainAdLdap {

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
	
	private String notifyEmail;

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

	public String getNotifyEmail() {
		return notifyEmail;
	}

	public void setNotifyEmail(String notifyEmail) {
		this.notifyEmail = notifyEmail;
	}

}
