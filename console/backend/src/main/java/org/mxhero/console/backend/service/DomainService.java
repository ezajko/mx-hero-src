package org.mxhero.console.backend.service;

import java.util.Collection;

import org.mxhero.console.backend.vo.DomainAdLdapVO;
import org.mxhero.console.backend.vo.DomainVO;
import org.springframework.security.access.annotation.Secured;

public interface DomainService {

	@Secured("ROLE_ADMIN")
	Collection<DomainVO> findAll();
	
	@Secured("ROLE_ADMIN")
	void remove(String domain);
	
	@Secured("ROLE_ADMIN")
	void insert(DomainVO domainVO,Boolean hasAdmin, String password, String email);
	
	@Secured("ROLE_ADMIN")
	void edit(DomainVO domainVO,Boolean hasAdmin, String password, String email);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	public DomainAdLdapVO insertAdLdap(DomainAdLdapVO adLdapVO);

	@Secured("ROLE_DOMAIN_ADMIN")
	public DomainAdLdapVO editAdLdap(DomainAdLdapVO adLdapVO);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	public void removeAdLdap(String domainId);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	public DomainAdLdapVO refreshAdLdap(String domainId);
	
}
