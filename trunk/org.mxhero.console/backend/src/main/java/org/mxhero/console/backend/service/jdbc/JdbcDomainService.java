package org.mxhero.console.backend.service.jdbc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;



import org.mxhero.console.backend.infrastructure.BusinessException;
import org.mxhero.console.backend.repository.DomainAdLdapRepository;
import org.mxhero.console.backend.repository.DomainRepository;
import org.mxhero.console.backend.repository.EmailAccountRepository;
import org.mxhero.console.backend.repository.FeatureRuleRepository;
import org.mxhero.console.backend.repository.GroupRepository;
import org.mxhero.console.backend.repository.SystemPropertyRepository;
import org.mxhero.console.backend.repository.UserRepository;
import org.mxhero.console.backend.service.DomainService;
import org.mxhero.console.backend.vo.ApplicationUserVO;
import org.mxhero.console.backend.vo.DomainAdLdapVO;
import org.mxhero.console.backend.vo.DomainVO;
import org.mxhero.console.backend.vo.LdapAccountVO;
import org.mxhero.console.backend.infrastructure.ADSource;
import org.mxhero.console.backend.infrastructure.ADSource.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("domainService")
@RemotingDestination(channels={"flex-amf"})
public class JdbcDomainService implements DomainService {

	public static final String DOMAIN_ALREADY_EXISTS="domain.already.exists";
	
	public static final String DOMAIN_NOT_EXISTS="domain.not.exists";
	
	public static final String ALIAS_WITH_DOMAIN_NAME="alias.with.domain.name";
	
	public static final String ALIAS_OTHER_DOMAIN="alias.other.domain";
	
	public static final String ALIAS_ALREADY_EXISTS="alias.already.exists";
	
	private static final String DEFAULT_LANGUAGE="default.user.language";
	
	public static final String LDAP_ERROR="ldap.error";
	
	private PasswordEncoder encoder;
	
	private DomainRepository domainRepository;
	
	private DomainAdLdapRepository adLdapRepository;
	
	private EmailAccountRepository accountRepository;
	
	private SystemPropertyRepository propertiesRespository;
	
	private GroupRepository groupRepository;
	
	private FeatureRuleRepository ruleRepository;
	
	private UserRepository userRepository;
	
	
	@Autowired(required=true)
	public JdbcDomainService(PasswordEncoder encoder,
			DomainRepository domainRepository,
			DomainAdLdapRepository adLdapRepository,
			EmailAccountRepository accountRepository,
			SystemPropertyRepository propertiesRespository,
			GroupRepository groupRepository,
			FeatureRuleRepository ruleRepository, UserRepository userRepository) {
		this.encoder = encoder;
		this.domainRepository = domainRepository;
		this.adLdapRepository = adLdapRepository;
		this.accountRepository = accountRepository;
		this.propertiesRespository = propertiesRespository;
		this.groupRepository = groupRepository;
		this.ruleRepository = ruleRepository;
		this.userRepository = userRepository;
	}

	@Override
	public Collection<DomainVO> findAll() {
		List<DomainVO> domains= domainRepository.findAll();
		if(domains!=null && domains.size()>0){
			for(DomainVO domain : domains){
				domain.setAdLdap(adLdapRepository.findByDomainId(domain.getDomain()));
				domain.setAliases(domainRepository.findAliases(domain.getDomain()));
			}
		}
		return domains;
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void remove(String domainId) {
		DomainVO domain = domainRepository.findById(domainId);
		if(domain!=null){
			ruleRepository.deleteByDomain(domainId);
			groupRepository.deleteByDomainId(domainId);
			accountRepository.deleteByDomainId(domainId);
			if(domain.getOwner()!=null){
				userRepository.delete(domain.getOwner().getId());
			}
			adLdapRepository.deleteByDomainId(domainId);
			domainRepository.delete(domainId);
		}
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void insert(DomainVO domainVO, Boolean hasAdmin, String password, String email) {
		
		if(domainRepository.findById(domainVO.getDomain())!=null){
			throw new BusinessException(DOMAIN_ALREADY_EXISTS);
		}
		
		domainRepository.insert(domainVO);
		if(hasAdmin){
			ApplicationUserVO user = new ApplicationUserVO();
			user.setName(domainVO.getDomain());
			user.setUserName(domainVO.getDomain());
			user.setLocale(propertiesRespository.findById(DEFAULT_LANGUAGE).getPropertyValue());
			user.setDomain(domainVO);
			user.setLastName(domainVO.getDomain());
			user.setName(domainVO.getDomain());
			user.setCreationDate(Calendar.getInstance());
			user.setSoundsEnabled(false);
			user.setNotifyEmail(email);
			userRepository.insert(user, encoder.encodePassword(password, null));		
		}	
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void edit(DomainVO domainVO, Boolean hasAdmin, String password,
			String email) {
		
		DomainVO domain = domainRepository.findById(domainVO.getDomain());
		if( domain == null){
			throw new BusinessException(DOMAIN_NOT_EXISTS);
		}

		if(hasAdmin){
			if(domain.getOwner()==null){
				ApplicationUserVO user = new ApplicationUserVO();
				user.setName(domainVO.getDomain());
				user.setUserName(domainVO.getDomain());
				user.setLocale(propertiesRespository.findById(DEFAULT_LANGUAGE).getPropertyValue());
				user.setDomain(domainVO);
				user.setLastName(domainVO.getDomain());
				user.setName(domainVO.getDomain());
				user.setCreationDate(Calendar.getInstance());
				user.setSoundsEnabled(false);
				user.setNotifyEmail(email);
				userRepository.insert(user, encoder.encodePassword(password, null));		
			} else {
				ApplicationUserVO user = userRepository.finbByUserName(domain.getOwner().getEmail());
				user.setNotifyEmail(email);
				userRepository.update(user);
				if(password!=null && !password.isEmpty()){
					userRepository.setPassword(encoder.encodePassword(password, null), user.getId());
				}
			}
		} else {
			if(domain.getOwner()!=null){
				userRepository.delete(domain.getOwner().getId());
			}
		}
		
		domainRepository.update(domainVO);
	}
	
	public DomainAdLdapVO insertAdLdap(DomainAdLdapVO adLdapVO){
		return adLdapRepository.insert(adLdapVO);
	}

	public DomainAdLdapVO editAdLdap(DomainAdLdapVO adLdapVO){
		return adLdapRepository.update(adLdapVO);
	}
	
	public void removeAdLdap(String domainId){
		adLdapRepository.deleteByDomainId(domainId);
	}
	
	public DomainAdLdapVO refreshAdLdap(String domainId){
		adLdapRepository.refresh(domainId);
		return adLdapRepository.findByDomainId(domainId);
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