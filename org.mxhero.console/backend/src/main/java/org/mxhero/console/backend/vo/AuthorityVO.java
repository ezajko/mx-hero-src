package org.mxhero.console.backend.vo;

import org.springframework.security.core.GrantedAuthority;

@SuppressWarnings("serial")
public class AuthorityVO implements GrantedAuthority{

	public static final String ROLE_DOMAIN_ADMIN = "ROLE_DOMAIN_ADMIN";
	
	private Integer id;
	
	private String authority;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

}
