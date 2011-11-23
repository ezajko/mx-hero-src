package org.mxhero.console.backend.repository;

import org.mxhero.console.backend.vo.DomainAdLdapVO;

public interface DomainAdLdapRepository {

	DomainAdLdapVO findByDomainId(String domainId);
	
	DomainAdLdapVO update(DomainAdLdapVO domainAdLdapVO);
	
	DomainAdLdapVO insert(DomainAdLdapVO domainAdLdapVO);

	void deleteByDomainId(String domainId);
	
	void refresh(String domainId);
	
}
