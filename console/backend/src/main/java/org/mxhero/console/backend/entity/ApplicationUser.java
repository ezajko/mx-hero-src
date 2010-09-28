package org.mxhero.console.backend.entity;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="app_users")
public class ApplicationUser{

	private static final long serialVersionUID = 3307754778675036756L;

	@Id
	private Integer id;
	
	@Column(name="name", length=30)
	private String name;
	
	@Column(name="last_name", length=30)
	private String lastName;
	
	@Column(name="notify_email", length=50)
	private String notifyEmail;
	
	@Column(name="password", nullable=false, length=20)
	private String password;
	
	@Column(name="userName", nullable=false, length=20)
	private String userName;
	
	@Column(name="last_login")
	private Calendar lastLogin;
	
	@Column(name="last_password_update")
	private Calendar lastPasswordUpdate;
	
	@Column(name="creation", nullable=false)
	private Calendar creationDate;
	
	@Column(name="valid_until")
	private Calendar validUntil;
		
	@Column(name="enabled", nullable=false)
	private boolean enabled;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.EAGER)
	private Set<Authority> authorities;
	
	public Calendar getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Calendar lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Calendar getLastPasswordUpdate() {
		return lastPasswordUpdate;
	}

	public void setLastPasswordUpdate(Calendar lastPasswordUpdate) {
		this.lastPasswordUpdate = lastPasswordUpdate;
	}

	public Calendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	public Calendar getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(Calendar validUntil) {
		this.validUntil = validUntil;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

}
