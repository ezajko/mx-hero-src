package org.mxhero.console.backend.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name="authorities")
public class Authority implements GrantedAuthority{

	private static final long serialVersionUID = 7734291471393522361L;

	@Id
	private Integer id;
	
	@Column(name="authority", nullable=false, length=50)
	private String authority;
	
	@ManyToMany(mappedBy="authorities", fetch=FetchType.LAZY)
	private Set<ApplicationUser> applicationUsers;
	
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

	public Set<ApplicationUser> getApplicationUsers() {
		return applicationUsers;
	}

	public void setApplicationUsers(Set<ApplicationUser> applicationUsers) {
		this.applicationUsers = applicationUsers;
	}

}
