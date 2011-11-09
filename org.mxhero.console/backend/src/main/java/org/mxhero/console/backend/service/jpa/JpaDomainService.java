package org.mxhero.console.backend.service.jpa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.mxhero.console.backend.dao.AliasAccountDao;
import org.mxhero.console.backend.dao.AuthorityDao;
import org.mxhero.console.backend.dao.DomainAliasDao;
import org.mxhero.console.backend.dao.DomainDao;
import org.mxhero.console.backend.dao.EmailAccountDao;
import org.mxhero.console.backend.dao.FeatureRuleDao;
import org.mxhero.console.backend.dao.SystemPropertyDao;
import org.mxhero.console.backend.entity.AliasAccount;
import org.mxhero.console.backend.entity.ApplicationUser;
import org.mxhero.console.backend.entity.Authority;
import org.mxhero.console.backend.entity.Domain;
import org.mxhero.console.backend.entity.DomainAdLdap;
import org.mxhero.console.backend.entity.DomainAlias;
import org.mxhero.console.backend.entity.EmailAccount;
import org.mxhero.console.backend.entity.FeatureRule;
import org.mxhero.console.backend.infrastructure.BusinessException;
import org.mxhero.console.backend.service.DomainService;
import org.mxhero.console.backend.service.FeatureService;
import org.mxhero.console.backend.translator.DomainAdLdaoTranslator;
import org.mxhero.console.backend.translator.DomainTranslator;
import org.mxhero.console.backend.vo.DomainAdLdapVO;
import org.mxhero.console.backend.vo.DomainVO;
import org.mxhero.console.backend.vo.LdapAccountVO;
import org.mxhero.console.backend.infrastructure.ADSource;
import org.mxhero.console.backend.infrastructure.ADSource.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.synyx.hades.domain.Sort;

@Service("domainService")
@RemotingDestination(channels={"flex-amf"})
public class JpaDomainService implements DomainService {

	public static final String DOMAIN_ALREADY_EXISTS="domain.already.exists";
	
	public static final String DOMAIN_NOT_EXISTS="domain.not.exists";
	
	public static final String ALIAS_WITH_DOMAIN_NAME="alias.with.domain.name";
	
	public static final String ALIAS_OTHER_DOMAIN="alias.other.domain";
	
	public static final String ALIAS_ALREADY_EXISTS="alias.already.exists";
	
	private static final String DEFAULT_LANGUAGE="default.user.language";
	
	public static final String LDAP_ERROR="ldap.error";
	
	private DomainDao dao;
	
	private AuthorityDao authorityDao;
	
	private DomainAliasDao domainaliasDao;
	
	private DomainTranslator domainTranslator;
	
	private SystemPropertyDao systemPropertyDao;
	
	private PasswordEncoder encoder;
	
	private FeatureService featureService;
	
	private FeatureRuleDao featureRuleDao;
	
	private AliasAccountDao aliasAccountDao;
	
	private DomainAdLdaoTranslator adLdapTranslator;
	
	private EmailAccountDao emailAccountDao;
	
	@Autowired(required=true)
	public JpaDomainService(DomainDao dao, AuthorityDao authorityDao,
			DomainAliasDao domainaliasDao, DomainTranslator domainTranslator,
			PasswordEncoder encoder,
			SystemPropertyDao systemPropertyDao,
			FeatureService featureService,
			FeatureRuleDao featureRuleDao,
			AliasAccountDao aliasAccountDao,
			DomainAdLdaoTranslator adLdapTranslator,
			EmailAccountDao emailAccountDao) {
		super();
		this.dao = dao;
		this.authorityDao = authorityDao;
		this.domainaliasDao = domainaliasDao;
		this.domainTranslator = domainTranslator;
		this.encoder = encoder;
		this.systemPropertyDao = systemPropertyDao;
		this.featureService = featureService;
		this.featureRuleDao = featureRuleDao;
		this.aliasAccountDao = aliasAccountDao;
		this.adLdapTranslator = adLdapTranslator;
		this.emailAccountDao = emailAccountDao;
	}

	@Override
	public Collection<DomainVO> findAll() {
		Collection<Domain> domains = dao.readAll(new Sort("domain"));
		Collection<DomainVO> domainsVO = domainTranslator.translate(domains);
		return domainsVO;
	}

	@Override
	public void remove(String id) {
		Domain domain = dao.readByPrimaryKey(id);
		
		if(domain!=null){
			for(FeatureRule rule : domain.getRules()){
				featureService.remove(rule.getId());
			}
			for(FeatureRule rule : featureRuleDao.findByDirectionTypeDomainId("domain",domain.getDomain())){
				featureService.remove(rule.getId());
			}
			
			domain = dao.readByPrimaryKey(id);
			
			aliasAccountDao.deleteAliasAccountByDomain(domain.getDomain());
			dao.delete(domain);
		}
		
	}

	@Override
	public void insert(DomainVO domainVO, Boolean hasAdmin, String password,
			String email) {
		Domain domain = null;
		
		if(dao.readByPrimaryKey(domainVO.getDomain()) != null){
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
			user.setLocale(systemPropertyDao.findByKey(DEFAULT_LANGUAGE).getPropertyValue());
			user.setNotifyEmail(email);
			user.setPassword(encoder.encodePassword(password, null));
			user.setUserName(domainVO.getDomain());
			domain.setOwner(user);
			user.setDomain(domain);
		}

		domain.setAliases(new HashSet<DomainAlias>());
		
		DomainAlias domainAlias = new DomainAlias();
		domainAlias.setAlias(domain.getDomain());
		domainAlias.setCreatedDate(Calendar.getInstance());
		domainAlias.setDomain(domain);
		domain.getAliases().add(domainAlias);
		
		dao.save(domain);		
	}

	@Override
	public void edit(DomainVO domainVO, Boolean hasAdmin, String password,
			String email) {
		Domain domain = dao.readByPrimaryKey(domainVO.getDomain());
		
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
				user.setLocale(systemPropertyDao.findByKey(DEFAULT_LANGUAGE).getPropertyValue());
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
		
		//check domainAlias for remove
		if(domain.getAliases()!=null){
			Collection<DomainAlias> domainAliases = new HashSet<DomainAlias>();
			domainAliases.addAll(domain.getAliases());
			for(DomainAlias alias : domainAliases){
				boolean remove = true;
				for(String aliasVO : domainVO.getAliases()){
					if(aliasVO.trim().toLowerCase().equalsIgnoreCase(alias.getAlias())){
						remove=false;
						domainVO.getAliases().remove(aliasVO);
						break;
					}
				}
				if(remove){
					domain.getAliases().remove(alias);
					aliasAccountDao.deleteAliasAccountByDomainAlias(alias.getAlias());
					domainaliasDao.deleteDomainAlias(alias.getAlias(), alias.getDomain().getDomain());
				}
			}
		}
		
		//at this point only new aliases are located on the VO
		for(String aliasVO : domainVO.getAliases()){
			DomainAlias newAlias = null;
			newAlias = domainaliasDao.readByPrimaryKey(aliasVO);
			Domain domainCheck = dao.readByPrimaryKey(aliasVO);
			if(domainCheck!=null && !domainCheck.getDomain().equalsIgnoreCase(domain.getDomain())){
				throw new BusinessException(ALIAS_WITH_DOMAIN_NAME);
			}
			if(newAlias!=null){
				throw new BusinessException(ALIAS_ALREADY_EXISTS);
			}
			newAlias = new DomainAlias();
			newAlias.setDomain(domain);
			newAlias.setAlias(aliasVO);
			newAlias.setCreatedDate(Calendar.getInstance());
			domain.getAliases().add(newAlias);
		}
		
		dao.save(domain);
	}
	
	public DomainAdLdapVO insertAdLdap(DomainAdLdapVO adLdapVO){
		DomainAdLdap entity = new DomainAdLdap();
		Domain domain = dao.readByPrimaryKey(adLdapVO.getDomainId());
		entity.setAddres(adLdapVO.getAddres());
		entity.setBase(adLdapVO.getBase());
		entity.setDirectoryType(adLdapVO.getDirectoryType());
		entity.setDomain(domain);
		entity.setDomainId(domain.getDomain());
		entity.setFilter(adLdapVO.getFilter());
		entity.setNextUpdate(Calendar.getInstance());
		entity.setOverrideFlag(adLdapVO.getOverrideFlag());
		entity.setPassword(adLdapVO.getPassword());
		entity.setPort(adLdapVO.getPort());
		entity.setSslFlag(adLdapVO.getSslFlag());
		entity.setUser(adLdapVO.getUser());
		entity.setDnAuthenticate(adLdapVO.getDnAuthenticate());
		domain.setAdLdap(entity);
		domain = dao.save(domain);
		return adLdapTranslator.translate(domain.getAdLdap());
	}

	public DomainAdLdapVO editAdLdap(DomainAdLdapVO adLdapVO){
		DomainAdLdap entity = dao.readByPrimaryKey(adLdapVO.getDomainId()).getAdLdap();
		
		entity.setAddres(adLdapVO.getAddres());
		entity.setBase(adLdapVO.getBase());
		entity.setDirectoryType(adLdapVO.getDirectoryType());
		entity.setFilter(adLdapVO.getFilter());
		entity.setNextUpdate(Calendar.getInstance());
		entity.setOverrideFlag(adLdapVO.getOverrideFlag());
		entity.setPassword(adLdapVO.getPassword());
		entity.setPort(adLdapVO.getPort());
		entity.setSslFlag(adLdapVO.getSslFlag());
		entity.setUser(adLdapVO.getUser());
		entity.setDnAuthenticate(adLdapVO.getDnAuthenticate());
	
		return adLdapTranslator.translate(dao.save(entity.getDomain()).getAdLdap());
	}
	
	public void removeAdLdap(String domainId){
		Domain domain = dao.readByPrimaryKey(domainId);
		if(domain.getAdLdap()!=null){
			domain.getAdLdap().setDomain(null);
			domain.getAdLdap().setDomainId(null);
			domain.setAdLdap(null);
			this.dao.save(domain);
			Collection<EmailAccount> accounts = this.emailAccountDao.finbAllByDomainId(domain.getDomain());
			
			if(accounts!=null){
				for(EmailAccount account : accounts){
					account.setDataSource(EmailAccount.MANUAL);
					if(account.getAliases()!=null){
						for(AliasAccount alias : account.getAliases()){
							alias.setDataSource(EmailAccount.MANUAL);
						}
					}
				}
			}
			this.emailAccountDao.save(accounts);
		}
	}
	
	public DomainAdLdapVO refreshAdLdap(String domainId){
		DomainAdLdap adLdap = null;
		Domain domain = dao.readByPrimaryKey(domainId);
		if(domain.getAdLdap()!=null){
			domain.getAdLdap().setNextUpdate(Calendar.getInstance());
			domain.getAdLdap().setError(null);
			adLdap = this.dao.save(domain).getAdLdap();
		}
		return adLdapTranslator.translate(adLdap);
	}

	@Override
	public Collection<LdapAccountVO> testAdLdap(DomainAdLdapVO adLdapVO) {
		Collection<LdapAccountVO> returnAccounts = null;
		try {
			
			String filter = adLdapVO.getFilter();
			if(adLdapVO.getDirectoryType().equalsIgnoreCase(ADSource.ZIMBRA_TYPE)){
				filter = ADSource.ZIMBRA_FILTER;
			}else if(adLdapVO.getDirectoryType().equalsIgnoreCase(ADSource.EXCHANGE_TYPE)){
				filter = ADSource.EXCHANGE_FILTER;
			}
			ADSource source = new ADSource(adLdapVO.getAddres(), adLdapVO.getPort(), adLdapVO.getUser(), adLdapVO.getPassword(), adLdapVO.getSslFlag(), adLdapVO.getBase());
			
			List<Account> accounts = source.getAllPersonNames(filter);
			if(accounts!=null && accounts.size()>0){
				returnAccounts = new ArrayList<LdapAccountVO>();
				for(Account account : accounts){
					LdapAccountVO newAccount = new LdapAccountVO();
					newAccount.setUid(account.getUid());
					newAccount.setEmails(account.getMails());
					returnAccounts.add(newAccount);
				}
			}
			
		} catch (Exception e) {
			throw new BusinessException(LDAP_ERROR,e.getMessage());
		}
		return returnAccounts;
	}
}
