package org.mxhero.console.backend.vo;

import java.util.Calendar;
import java.util.Collection;

import org.mxhero.console.backend.vo.AuthorityVO;
import org.mxhero.console.backend.vo.DomainVO;

public class ApplicationUserVO {

	private Integer id;
	
	private String name;
	
	private String lastName;
	
	private String notifyEmail;
	
	private String userName;
	
	private Calendar creationDate;
	
	private String locale;
	
	private Collection<AuthorityVO> authorities;
	
	private DomainVO domain;
	
	private Boolean soundsEnabled;
	
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
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
	
	public Calendar getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}
	
	public String getLocale() {
		return locale;
	}
	
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public Collection<AuthorityVO> getAuthorities() {
		return authorities;
	}
	
	public void setAuthorities(Collection<AuthorityVO> authorities) {
		this.authorities = authorities;
	}
	
	public DomainVO getDomain() {
		return domain;
	}
	
	public void setDomain(DomainVO domain) {
		this.domain = domain;
	}

	public Boolean getSoundsEnabled() {
		return soundsEnabled;
	}

	public void setSoundsEnabled(Boolean soundsEnabled) {
		this.soundsEnabled = soundsEnabled;
	}

}
