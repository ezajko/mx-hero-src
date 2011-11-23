package org.mxhero.console.backend.security;

import java.util.Collection;

import org.mxhero.console.backend.vo.AuthorityVO;
import org.springframework.security.core.userdetails.User;

public class CustomUser extends User {

	private static final long serialVersionUID = -1948876312378134622L;
	
	public CustomUser(String userName, String password, Boolean enabled, Collection<AuthorityVO> authorities){
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
}
