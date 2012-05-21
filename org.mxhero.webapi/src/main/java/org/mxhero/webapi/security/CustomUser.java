package org.mxhero.webapi.security;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUser extends User implements Serializable{

	private static final long serialVersionUID = -1948876312378134622L;
	
	private String domain;
	
	private String account;
	
	public CustomUser(String userName, String password, Boolean enabled, Collection<GrantedAuthority> authorities){
		super(userName, password, enabled, true, true, true, authorities);
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
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
