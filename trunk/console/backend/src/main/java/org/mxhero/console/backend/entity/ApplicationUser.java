package org.mxhero.console.backend.entity;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="app_users",schema="mxhero")
public class ApplicationUser {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name="name", length=30)
	private String name;
	
	@Column(name="last_name", length=30)
	private String lastName;
	
	@Column(name="notify_email", length=50)
	private String notifyEmail;
	
	@Column(name="password",  nullable=false, length=100)
	private String password;
	
	@Column(name="userName", unique=true, nullable=false, length=20)
	private String userName;

	@Column(name="last_password_update")
	private Calendar lastPasswordUpdate;
	
	@Column(name="creation", nullable=false)
	private Calendar creationDate;
	
	@Column(name="enabled", nullable=false)
	private boolean enabled;
	
	@Column(name="locale", nullable=false)
	private String locale;
	
	@Column(name="sounds_enabled", nullable=false)
	private boolean soundsEnabled = true;
	
	@ManyToMany(cascade = {CascadeType.MERGE}, fetch=FetchType.EAGER)
	@JoinTable(
        name="app_users_authorities",
        joinColumns=@JoinColumn(name="app_users_id"),
        inverseJoinColumns=@JoinColumn(name="authorities_id")
    )
	private Set<Authority> authorities;
	
	@OneToOne(fetch=FetchType.EAGER, cascade={CascadeType.MERGE,CascadeType.PERSIST})
	@JoinColumn(name="domain_id")
	private Domain domain;
	

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

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public boolean isSoundsEnabled() {
		return soundsEnabled;
	}

	public void setSoundsEnabled(boolean soundsEnabled) {
		this.soundsEnabled = soundsEnabled;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
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
		ApplicationUser other = (ApplicationUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
