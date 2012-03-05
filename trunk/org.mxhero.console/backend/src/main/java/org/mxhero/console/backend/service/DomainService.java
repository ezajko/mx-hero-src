package org.mxhero.console.backend.service;

import java.util.Collection;

import org.mxhero.console.backend.vo.DomainAdLdapVO;
import org.mxhero.console.backend.vo.DomainVO;
import org.mxhero.console.backend.vo.LdapAccountVO;
import org.mxhero.console.backend.vo.PageVO;

public interface DomainService {

	PageVO findAll(String domainName, int pageNo, int pageSize);
	
	void remove(String domain);
	
	void insert(DomainVO domainVO,Boolean hasAdmin, String password, String email);
	
	void edit(DomainVO domainVO,Boolean hasAdmin, String password, String email);
	
	public DomainAdLdapVO insertAdLdap(DomainAdLdapVO adLdapVO);

	public DomainAdLdapVO editAdLdap(DomainAdLdapVO adLdapVO);
	
	public void removeAdLdap(String domainId);

	public DomainAdLdapVO refreshAdLdap(String domainId);

	public Collection<LdapAccountVO> testAdLdap(DomainAdLdapVO adLdapVO);
	
}
