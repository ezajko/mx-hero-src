package org.mxhero.webapi.vo;

import java.util.Calendar;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserVO {

	private String name;
	private String lastName;
	private String notifyEmail;
	private String userName;
	private Calendar created;
	private String locale;
	private String password;
	private Collection<String> authorities;
	private Boolean soundsEnabled;
	private String domain;
	private String account;

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getNotifyEmail() {
		return notifyEmail;
	}
	
	public void setNotifyEmail(String notifyEmail) {
		this.notifyEmail = notifyEmail;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public Calendar getCreated() {
		return created;
	}
	
	public void setCreated(Calendar created) {
		this.created = created;
	}
	
	public String getLocale() {
		return locale;
	}
	
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Collection<String> getAuthorities() {
		return authorities;
	}
	
	public void setAuthorities(Collection<String> authorities) {
		this.authorities = authorities;
	}
	
	public Boolean getSoundsEnabled() {
		return soundsEnabled;
	}
	
	public void setSoundsEnabled(Boolean soundsEnabled) {
		this.soundsEnabled = soundsEnabled;
	}
	
	public String getDomain() {
		return domain;
	}
	
	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
}
