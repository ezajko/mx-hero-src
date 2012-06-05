package org.mxhero.webapi.vo;

import java.util.Calendar;
import java.util.List;

public class UserVO {

	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_DOMAIN_ADMIN = "ROLE_DOMAIN_ADMIN";
	public static final String ROLE_DOMAIN_ACCOUNT = "ROLE_DOMAIN_ACCOUNT";
	
	private String name;
	private String lastName;
	private String notifyEmail;
	private String userName;
	private Calendar created;
	private String locale;
	private String password;
	private List<String> authorities;
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
	
	public List<String> getAuthorities() {
		return authorities;
	}
	
	public void setAuthorities(List<String> authorities) {
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
		UserVO other = (UserVO) obj;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	
}
