package org.mxhero.webapi.service.jdbc;

import org.mxhero.webapi.repository.LdapRepository;
import org.mxhero.webapi.service.LdapService;
import org.mxhero.webapi.service.exception.ConflictResourceException;
import org.mxhero.webapi.service.exception.UnknownResourceException;
import org.mxhero.webapi.vo.LdapVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("jdbcLdapService")
public class JdbcLdapService implements LdapService{

	private LdapRepository ldapRepository;
	
	@Autowired(required=true) 
	public JdbcLdapService(LdapRepository ldapRepository) {
		this.ldapRepository = ldapRepository;
	}

	@Override
	@Transactional(readOnly=false)
	public LdapVO create(LdapVO ldapVO) {
		if(ldapRepository.findByDomainId(ldapVO.getDomain())!=null){
			throw new ConflictResourceException("domain.ldap.already.exists");
		}
		return ldapRepository.insert(ldapVO);
	}

	@Override
	@Transactional(readOnly=true)
	public LdapVO read(String domain) {
		LdapVO result = ldapRepository.findByDomainId(domain);
		if(result==null){
			throw new UnknownResourceException("domain.ldap.not.found");
		}
		return result;
	}

	@Override
	@Transactional(readOnly=false)
	public void update(LdapVO ldapVO) {
		if(ldapRepository.findByDomainId(ldapVO.getDomain())==null){
			throw new UnknownResourceException("domain.ldap.not.found");
		}
		ldapRepository.update(ldapVO);
	}

	@Override
	@Transactional(readOnly=false)
	public void delete(String domain) {
		if(ldapRepository.findByDomainId(domain)==null){
			throw new UnknownResourceException("domain.ldap.not.found");
		}
		ldapRepository.deleteByDomainId(domain);
	}

}
