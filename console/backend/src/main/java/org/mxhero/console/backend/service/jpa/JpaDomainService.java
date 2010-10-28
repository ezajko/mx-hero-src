package org.mxhero.console.backend.service.jpa;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

import org.mxhero.console.backend.dao.AuthorityDao;
import org.mxhero.console.backend.dao.DomainDao;
import org.mxhero.console.backend.entity.ApplicationUser;
import org.mxhero.console.backend.entity.Authority;
import org.mxhero.console.backend.entity.Domain;
import org.mxhero.console.backend.infrastructure.BusinessException;
import org.mxhero.console.backend.service.DomainService;
import org.mxhero.console.backend.translator.DomainTranslator;
import org.mxhero.console.backend.vo.DomainVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("domainService")
@RemotingDestination(channels={"flex-amf"})
public class JpaDomainService implements DomainService {

	public static final String DOMAIN_ALREADY_EXISTS="domain.already.exists";
	
	public static final String DOMAIN_NOT_EXISTS="domain.not.exists";
	
	private DomainDao dao;
	
	private AuthorityDao authorityDao;
	
	private DomainTranslator domainTranslator;
	
	private PasswordEncoder encoder;
	
	@Autowired(required=true)
	public JpaDomainService(DomainDao dao, DomainTranslator domainTranslator, AuthorityDao authorityDao, PasswordEncoder encoder) {
		this.dao = dao;
		this.authorityDao=authorityDao;
		this.domainTranslator=domainTranslator;
		this.encoder=encoder;
	}

	@Override
	public Collection<DomainVO> findAll() {
		Collection<Domain> domains = dao.readAll();
		Collection<DomainVO> domainsVO = domainTranslator.translate(domains);
		return domainsVO;
	}

	@Override
	public void remove(Integer id) {
		Domain domain = dao.readByPrimaryKey(id);
		dao.delete(domain);
	}

	@Override
	public void insert(DomainVO domainVO, Boolean hasAdmin, String password,
			String email) {
		Domain domain = null;
		
		if(dao.finbByDomain(domainVO.getDomain()) != null){
			throw new BusinessException(DOMAIN_ALREADY_EXISTS);
		}
		
		domain = new Domain();
		domain.setCreationDate(Calendar.getInstance());
		domain.setDomain(domainVO.getDomain());
		domain.setServer(domainVO.getServer());
		domain.setUpdatesDate(Calendar.getInstance());
		
		if(hasAdmin){
			Authority authority = authorityDao.finbByAuthority(Authority.ROLE_DOMAIN_ADMIN);
			ApplicationUser user = new ApplicationUser();
			user.setAuthorities(new HashSet<Authority>());
			user.getAuthorities().add(authority);
			user.setCreationDate(Calendar.getInstance());
			user.setEnabled(true);
			user.setLastName(domainVO.getDomain());
			user.setLocale(ApplicationUser.DEFAULT_LOCALE);
			user.setName(domainVO.getDomain());
			user.setNotifyEmail(email);
			user.setPassword(encoder.encodePassword(password, null));
			user.setUserName(domainVO.getDomain());
			domain.setOwner(user);
			user.setDomain(domain);
		}
		dao.save(domain);
	}

	@Override
	public void edit(DomainVO domainVO, Boolean hasAdmin, String password,
			String email) {
		Domain domain = dao.finbByDomain(domainVO.getDomain());
		
		if( domain == null){
			throw new BusinessException(DOMAIN_NOT_EXISTS);
		}

		domain.setServer(domainVO.getServer());
		domain.setUpdatesDate(Calendar.getInstance());
		
		if(hasAdmin){
			if(domain.getOwner()==null){
				Authority authority = authorityDao.finbByAuthority(Authority.ROLE_DOMAIN_ADMIN);
				ApplicationUser user = new ApplicationUser();
				user.setAuthorities(new HashSet<Authority>());
				user.getAuthorities().add(authority);
				user.setCreationDate(Calendar.getInstance());
				user.setEnabled(true);
				user.setLastName(domainVO.getDomain());
				user.setLocale(ApplicationUser.DEFAULT_LOCALE);
				user.setName(domainVO.getDomain());
				user.setNotifyEmail(email);
				user.setPassword(encoder.encodePassword(password, null));
				user.setUserName(domainVO.getDomain());
				domain.setOwner(user);
				user.setDomain(domain);				
			} else {
				domain.getOwner().setNotifyEmail(email);
				domain.getOwner().setPassword(encoder.encodePassword(password, null));
			}
		} else {
			domain.setOwner(null);
		}
		dao.save(domain);
	}

}
