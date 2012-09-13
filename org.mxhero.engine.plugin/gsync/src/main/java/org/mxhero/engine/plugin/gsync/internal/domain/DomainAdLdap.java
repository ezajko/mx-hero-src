/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mxhero.engine.plugin.gsync.internal.domain;

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
