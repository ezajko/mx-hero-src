package org.mxhero.console.backend.service;

import java.util.Calendar;

import org.mxhero.console.backend.entity.ApplicationUser;
import org.springframework.security.core.userdetails.User;

public class CustomUser extends User {

	private static final long serialVersionUID = -1948876312378134622L;
	
	private ApplicationUser user;
	
	public CustomUser(ApplicationUser user){
		super(user.getUserName(), user.getPassword(), user.isEnabled(), true, true, true, user.getAuthorities());
		this.user = user;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return user.getValidUntil().after(Calendar.getInstance());
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
}
