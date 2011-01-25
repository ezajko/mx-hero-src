package org.mxhero.console.backend.service.jpa;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

import org.mxhero.console.backend.dao.AuthorityDao;
import org.mxhero.console.backend.dao.DomainAliasDao;
import org.mxhero.console.backend.dao.DomainDao;
import org.mxhero.console.backend.dao.FeatureRuleDao;
import org.mxhero.console.backend.dao.SystemPropertyDao;
import org.mxhero.console.backend.entity.ApplicationUser;
import org.mxhero.console.backend.entity.Authority;
import org.mxhero.console.backend.entity.Domain;
import org.mxhero.console.backend.entity.DomainAlias;
import org.mxhero.console.backend.entity.EmailAccount;
import org.mxhero.console.backend.entity.FeatureRule;
import org.mxhero.console.backend.entity.Group;
import org.mxhero.console.backend.infrastructure.BusinessException;
import org.mxhero.console.backend.service.DomainService;
import org.mxhero.console.backend.service.FeatureService;
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
	
	public static final String ALIAS_WITH_DOMAIN_NAME="alias.with.domain.name";
	
	public static final String ALIAS_OTHER_DOMAIN="alias.other.domain";
	
	private static final String DEFAULT_LANGUAGE="default.user.language";
	
	private DomainDao dao;
	
	private AuthorityDao authorityDao;
	
	private DomainAliasDao domainaliasDao;
	
	private DomainTranslator domainTranslator;
	
	private SystemPropertyDao systemPropertyDao;
	
	private PasswordEncoder encoder;
	
	private FeatureService featureService;
	
	private FeatureRuleDao featureRuleDao;
	
	@Autowired(required=true)
	public JpaDomainService(DomainDao dao, AuthorityDao authorityDao,
			DomainAliasDao domainaliasDao, DomainTranslator domainTranslator,
			PasswordEncoder encoder,
			SystemPropertyDao systemPropertyDao,
			FeatureService featureService,
			FeatureRuleDao featureRuleDao) {
		super();
		this.dao = dao;
		this.authorityDao = authorityDao;
		this.domainaliasDao = domainaliasDao;
		this.domainTranslator = domainTranslator;
		this.encoder = encoder;
		this.systemPropertyDao = systemPropertyDao;
		this.featureService = featureService;
		this.featureRuleDao = featureRuleDao;
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
		
		if(domain!=null){
			for(FeatureRule rule : domain.getRules()){
				featureService.remove(rule.getId());
			}
			for(FeatureRule rule : featureRuleDao.findByDirectionTypeAndValueId("domain",domain.getId())){
				featureService.remove(rule.getId());
			}
			
			for(Group group : domain.getGroups()){
				for(FeatureRule rule : featureRuleDao.findByDirectionTypeAndValueId("group",group.getId())){
					featureService.remove(rule.getId());
				}
			}
			
			for(EmailAccount account : domain.getEmailAccounts()){
				for(FeatureRule rule : featureRuleDao.findByDirectionTypeAndValueId("individual",account.getId())){
					featureService.remove(rule.getId());
				}
			}
			
			domain = dao.readByPrimaryKey(id);
			dao.delete(domain);
		}
		
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
		domain.setDomain(domainVO.getDomain().toLowerCase());
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
			user.setLocale(systemPropertyDao.findByKey(DEFAULT_LANGUAGE).getPropertyValue());
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
				user.setLocale(systemPropertyDao.findByKey(DEFAULT_LANGUAGE).getPropertyValue());
				user.setName(domainVO.getDomain());
				user.setNotifyEmail(email);
				user.setPassword(encoder.encodePassword(password, null));
				user.setUserName(domainVO.getDomain());
				domain.setOwner(user);
				user.setDomain(domain);				
			} else {
				domain.getOwner().setNotifyEmail(email);
				if(password!=null && !password.isEmpty()){
					domain.getOwner().setPassword(encoder.encodePassword(password, null));
				}
			}
		} else {
			domain.setOwner(null);
		}

		if(domainVO.getAliases()!=null && domainVO.getAliases().size()>0){
			domain.setAliases(new HashSet<DomainAlias>());
			for(String alias : domainVO.getAliases()){
				if(dao.finbByDomain(alias)!=null){
					throw new BusinessException(ALIAS_WITH_DOMAIN_NAME);
				}
				DomainAlias domainAlias = domainaliasDao.readByPrimaryKey(alias);
				
				if(domainAlias!=null){
					if(!domainAlias.getDomain().getId().equals(domainVO.getId())){
						throw new BusinessException(ALIAS_OTHER_DOMAIN);
					}
				} else {
					domainAlias = new DomainAlias();
					domainAlias.setAlias(alias.toLowerCase());
					domainAlias.setCreatedDate(Calendar.getInstance());
					domainAlias.setDomain(domain);
				}
				domain.getAliases().add(domainAlias);
			}

		}else{
			if(domain.getAliases()!=null){
				domain.getAliases().clear();
			}
		}
		dao.save(domain);
	}
}
