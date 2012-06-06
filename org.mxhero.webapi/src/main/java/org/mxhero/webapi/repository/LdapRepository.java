package org.mxhero.webapi.repository;

import org.mxhero.webapi.vo.LdapVO;


public interface LdapRepository {

	LdapVO findByDomainId(String domainId);
	
	LdapVO update(LdapVO domainAdLdapVO);
	
	LdapVO insert(LdapVO domainAdLdapVO);

	void deleteByDomainId(String domainId);
	
	void refresh(String domainId);
	
}
