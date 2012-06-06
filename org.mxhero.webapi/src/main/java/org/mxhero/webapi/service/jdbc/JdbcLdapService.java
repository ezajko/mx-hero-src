package org.mxhero.webapi.service.jdbc;

import org.mxhero.webapi.repository.LdapRepository;
import org.mxhero.webapi.service.LdapService;
import org.mxhero.webapi.service.exception.ConflictResourceException;
import org.mxhero.webapi.service.exception.UnknownResourceException;
import org.mxhero.webapi.vo.LdapVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("jdbcLdapService")
public class JdbcLdapService implements LdapService{

	private LdapRepository ldapRepository;
	
	@Autowired(required=true) 
	public JdbcLdapService(LdapRepository ldapRepository) {
		this.ldapRepository = ldapRepository;
	}

	@Override
	public LdapVO create(String domain, LdapVO domainVO) {
		if(ldapRepository.findByDomainId(domain)!=null){
			throw new ConflictResourceException("system.property.already.exists");
		}
		return ldapRepository.insert(domainVO);
	}

	@Override
	public LdapVO read(String domain) {
		LdapVO result = ldapRepository.findByDomainId(domain);
		if(result==null){
			throw new UnknownResourceException("domain.ldap.not.found");
		}
		return result;
	}

	@Override
	public void update(String domain, LdapVO ldapVO) {
		if(ldapRepository.findByDomainId(domain)!=null){
			throw new UnknownResourceException("domain.ldap.not.found");
		}
		ldapRepository.update(ldapVO);
	}

	@Override
	public void delete(String domain) {
		if(ldapRepository.findByDomainId(domain)!=null){
			throw new UnknownResourceException("domain.ldap.not.found");
		}
		ldapRepository.deleteByDomainId(domain);
	}

}
