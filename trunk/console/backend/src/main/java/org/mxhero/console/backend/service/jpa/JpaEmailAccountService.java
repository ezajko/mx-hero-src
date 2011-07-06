package org.mxhero.console.backend.service.jpa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

import javax.validation.ConstraintViolationException;

import org.mxhero.console.backend.dao.AliasAccountDao;
import org.mxhero.console.backend.dao.DomainAliasDao;
import org.mxhero.console.backend.dao.DomainDao;
import org.mxhero.console.backend.dao.EmailAccountDao;
import org.mxhero.console.backend.dao.FeatureRuleDao;
import org.mxhero.console.backend.dao.GroupDao;
import org.mxhero.console.backend.entity.AliasAccount;
import org.mxhero.console.backend.entity.AliasAccountPk;
import org.mxhero.console.backend.entity.DomainAlias;
import org.mxhero.console.backend.entity.EmailAccount;
import org.mxhero.console.backend.entity.EmailAccountPk;
import org.mxhero.console.backend.entity.FeatureRule;
import org.mxhero.console.backend.entity.GroupPk;
import org.mxhero.console.backend.infrastructure.BusinessException;
import org.mxhero.console.backend.service.EmailAccountService;
import org.mxhero.console.backend.service.FeatureService;
import org.mxhero.console.backend.translator.EmailAccountTranslator;
import org.mxhero.console.backend.vo.EmailAccountAliasVO;
import org.mxhero.console.backend.vo.EmailAccountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.synyx.hades.domain.Sort;


@Service("emailAccountService")
@RemotingDestination(channels={"flex-amf"})
public class JpaEmailAccountService implements EmailAccountService{

	public static final String EMAIL_ALREADY_EXISTS="email.already.exists";
	public static final String EMAIL_INVALID_FORMAT="email.invalid.format";
	public static final String ALIAS_ALREADY_EXISTS="alias.account.already.exists";
	public static final String ALIAS_NOT_IN_DOMAIN="alias.not.in.domain";
	public static final String ALIAS_NOT_EXISTS="alias.not.exists";
	
	private EmailAccountDao emailAccountDao;
	
	private DomainDao domainDao;
	
	private EmailAccountTranslator emailAccountTranslator;
	
	private GroupDao groupDao;
	
	private FeatureRuleDao featureRuleDao;
	
	private FeatureService featureService;
	
	private AliasAccountDao aliasDao;
	
	private DomainAliasDao domainAliasDao;
	
	@Autowired
	public JpaEmailAccountService(EmailAccountDao emailAccountDao,
			DomainDao domainDao, EmailAccountTranslator emailAccountTranslator,
			GroupDao groupDao,
			FeatureRuleDao featureRuleDao,
			FeatureService featureService,
			AliasAccountDao aliasDao,
			DomainAliasDao domainAliasDao) {
		this.emailAccountDao = emailAccountDao;
		this.domainDao = domainDao;
		this.emailAccountTranslator = emailAccountTranslator;
		this.groupDao = groupDao;
		this.featureRuleDao = featureRuleDao;
		this.featureService = featureService;
		this.aliasDao=aliasDao;
		this.domainAliasDao=domainAliasDao;
	}

	public Collection<EmailAccountVO> findPageBySpecs(String domainId, String email, String group) {

		boolean hasAccount=false;
		boolean hasGroup=false;
		
		if(email!=null && !email.trim().isEmpty()){
			hasAccount=true;
		}

		if(group!=null && !group.trim().isEmpty()){
			hasGroup=true;
		}

		Collection<EmailAccount> result = null;
		
		if(hasAccount && hasGroup){
			result = emailAccountDao.finbAllByDomainIdAndAccoundLikeAndGroup(domainId,  "%"+email+"%", group);
		}else if(hasAccount){
			result = emailAccountDao.finbAllByDomainIdAndAccoundLike(domainId, "%"+email+"%");
		}else if(hasGroup){
			result = emailAccountDao.findAllByGroupId(group, domainId);
		}else{
			result = emailAccountDao.finbAllByDomainId(domainId);
		}

		return emailAccountTranslator.translate(result);

	}

	@Override
	public void remove(String account,String domain) {
		EmailAccountPk pk = new EmailAccountPk();
		pk.setAccount(account);
		pk.setDomainId(domain);
		EmailAccount emailAccount= emailAccountDao.readByPrimaryKey(pk);
		
		if(emailAccount!=null){
			for(FeatureRule rule : featureRuleDao.findByDirectionTypeDomainIdAccountId("individual",emailAccount.getId().getDomainId(),emailAccount.getId().getAccount())){
				emailAccount.getDomain().getRules().remove(rule);
				featureService.remove(rule.getId());
			}
			aliasDao.deleteAliasAccountByAccount(account, domain);
			emailAccountDao.deleteAccount(account, domain);
		}
	}

	@Override
	public void edit(EmailAccountVO emailAccountVO) {
		EmailAccountPk pk = new EmailAccountPk();
		pk.setAccount(emailAccountVO.getAccount());
		pk.setDomainId(emailAccountVO.getDomain());
		
		EmailAccount emailAccount =emailAccountDao.readByPrimaryKey(pk);

		emailAccount.setUpdatedDate(Calendar.getInstance());
		if(emailAccountVO.getGroup()==null){
			emailAccount.setGroup(null);
		} else {
			emailAccount.setGroup(groupDao.readByPrimaryKey(new GroupPk(emailAccountVO.getGroup(),emailAccountVO.getDomain())));
		}
		
		//check ccountAlias for remove
		if(emailAccount.getAliases()!=null){
			Collection<AliasAccount> accountAliases = new HashSet<AliasAccount>();
			accountAliases.addAll(emailAccount.getAliases());
			for(AliasAccount alias : accountAliases){
				boolean remove = true;
				for(EmailAccountAliasVO aliasVO : emailAccountVO.getAliases()){
					if(aliasVO.getName().trim().equalsIgnoreCase(alias.getId().getAccountAlias())
							&& aliasVO.getDomain().trim().equalsIgnoreCase(alias.getId().getDomainAlias())){
						remove=false;
						emailAccountVO.getAliases().remove(aliasVO);
						break;
					}
				}
				if(remove){
					emailAccount.getAliases().remove(alias);
					aliasDao.deleteAliasAccountById(alias.getId().getAccountAlias(), alias.getId().getDomainAlias());
				}
			}
		}
		
		//at this point only new aliases are located on the VO
		for (EmailAccountAliasVO aliasVO : emailAccountVO.getAliases()){
			DomainAlias da = domainAliasDao.readByPrimaryKey(aliasVO.getDomain().trim().toLowerCase());
			if(da==null){
				throw new BusinessException(ALIAS_NOT_EXISTS);
			}
			if(!da.getDomain().getDomain().equalsIgnoreCase(emailAccount.getDomain().getDomain())){
				throw new BusinessException(ALIAS_NOT_IN_DOMAIN);
			}
			if(aliasDao.exists(new AliasAccountPk(aliasVO.getName().trim().toLowerCase(), aliasVO.getDomain().trim().toLowerCase()))){
				throw new BusinessException(ALIAS_ALREADY_EXISTS);
			}
			AliasAccount alias = new AliasAccount();
			alias.setId(new AliasAccountPk(aliasVO.getName().trim().toLowerCase(), aliasVO.getDomain().trim().toLowerCase()));
			alias.setDomainAlias(da);
			alias.setCreated(Calendar.getInstance());
			alias.setAccount(emailAccount);
			alias.setDataSource(EmailAccount.MANUAL);
				
			if(emailAccount.getAliases()==null){
				emailAccount.setAliases(new HashSet<AliasAccount>());
			}
			emailAccount.getAliases().add(alias);
			
		}
		
		emailAccountDao.save(emailAccount);
	}

	
	
	@Override
	public void insert(EmailAccountVO emailAccountVO, String domainId) {

		EmailAccountPk pk = new EmailAccountPk();
		pk.setAccount(emailAccountVO.getAccount().trim().toLowerCase());
		pk.setDomainId(emailAccountVO.getDomain().trim().toLowerCase());
		EmailAccount newEmailAccount = new EmailAccount();
		newEmailAccount.setUpdatedDate(Calendar.getInstance());
		newEmailAccount.setCreatedDate(Calendar.getInstance());
		newEmailAccount.setId(pk);
		newEmailAccount.setDomain(domainDao.readByPrimaryKey(domainId.trim().toLowerCase()));
		newEmailAccount.setAliases(new HashSet<AliasAccount>());
		newEmailAccount.setDataSource(EmailAccount.MANUAL);
		if(aliasDao.exists(new AliasAccountPk(emailAccountVO.getAccount().trim().toLowerCase(), emailAccountVO.getDomain().trim().toLowerCase()))){
			throw new BusinessException(ALIAS_ALREADY_EXISTS);
		}
		
		AliasAccount alias = new AliasAccount();
		alias.setId(new AliasAccountPk(emailAccountVO.getAccount().trim().toLowerCase(), emailAccountVO.getDomain().trim().toLowerCase()));
		alias.setDomainAlias(domainAliasDao.readByPrimaryKey(emailAccountVO.getDomain().trim().toLowerCase()));
		alias.setAccount(newEmailAccount);
		alias.setCreated(Calendar.getInstance());
		alias.setDataSource(EmailAccount.MANUAL);
		
		newEmailAccount.getAliases().add(alias);
		if(emailAccountVO.getGroup()==null){
			newEmailAccount.setGroup(null);
		} else {
			newEmailAccount.setGroup(groupDao.readByPrimaryKey(new GroupPk(emailAccountVO.getGroup(),emailAccountVO.getDomain())));
		}
		try{
			emailAccountDao.save(newEmailAccount);
		} catch (DataIntegrityViolationException e){
			throw new BusinessException(EMAIL_ALREADY_EXISTS);
		}
	}

	@Override
	@Transactional(readOnly=false)
	public Collection<EmailAccountVO> upload(Collection<EmailAccountVO> emailAccountVOs, String domainId,
			Boolean failOnError) {
		Collection<EmailAccount> newEmailAccounts = new ArrayList<EmailAccount>(emailAccountVOs.size());
		Collection<EmailAccountVO> errorEmailAccountsVOs = new ArrayList<EmailAccountVO>();

		if(failOnError){
			try{
				for(EmailAccountVO emailAccountVO : emailAccountVOs){
					newEmailAccounts.add(uploadAccount(emailAccountVO));
				}
				emailAccountDao.save(newEmailAccounts);
			}catch(DataIntegrityViolationException e){
				throw new BusinessException(EMAIL_ALREADY_EXISTS);
			}catch(ConstraintViolationException e){
				throw new BusinessException(EMAIL_INVALID_FORMAT);
			}
		}else{
			for(EmailAccountVO emailAccountVO : emailAccountVOs){
				try{
					emailAccountDao.save(uploadAccount(emailAccountVO));
				}catch(DataIntegrityViolationException e){
					errorEmailAccountsVOs.add(emailAccountVO);
				}catch(ConstraintViolationException e){
					errorEmailAccountsVOs.add(emailAccountVO);
				}catch(BusinessException e){
					errorEmailAccountsVOs.add(emailAccountVO);
				}
			}	
		}
		return errorEmailAccountsVOs;
	}
	
	
	private EmailAccount uploadAccount(EmailAccountVO accountVO){
		EmailAccount account = new EmailAccount();
		EmailAccountPk pk = new EmailAccountPk();

		pk.setAccount(accountVO.getAccount());
		pk.setDomainId(accountVO.getDomain());
		
		account.setId(pk);
		account.setGroup(null);
		account.setDataSource(EmailAccount.MANUAL);
		account.setUpdatedDate(Calendar.getInstance());
		account.setCreatedDate(Calendar.getInstance());
		account.setAliases(new HashSet<AliasAccount>());
		account.setDomain(domainDao.readByPrimaryKey(accountVO.getDomain()));
		
		AliasAccount principalAlias = new AliasAccount();
		principalAlias.setAccount(account);
		principalAlias.setDataSource(EmailAccount.MANUAL);
		principalAlias.setCreated(Calendar.getInstance());
		principalAlias.setId(new AliasAccountPk(account.getId().getAccount(), account.getId().getDomainId()));
		principalAlias.setDomainAlias(domainAliasDao.readByPrimaryKey(account.getId().getDomainId()));
		if(principalAlias.getDomainAlias()==null){
			throw new BusinessException(ALIAS_NOT_EXISTS);
		}else if(!principalAlias.getDomainAlias().getDomain().getDomain().equals(account.getDomain().getDomain())){
			throw new BusinessException(ALIAS_NOT_IN_DOMAIN);
		}
		account.getAliases().add(principalAlias);
		
		if(accountVO.getAliases()!=null){
			for(EmailAccountAliasVO aliasVO : accountVO.getAliases()){
				if(!aliasVO.getDomain().equals(account.getId().getDomainId())
					|| !aliasVO.getName().equals(account.getId().getAccount())){
					AliasAccount alias = new AliasAccount();
					alias.setAccount(account);
					alias.setDataSource(EmailAccount.MANUAL);
					alias.setCreated(Calendar.getInstance());
					alias.setDomainAlias(domainAliasDao.readByPrimaryKey(aliasVO.getDomain()));
					if(alias.getDomainAlias()==null){
						throw new BusinessException(ALIAS_NOT_EXISTS);
					}else if(!alias.getDomainAlias().getDomain().getDomain().equals(account.getDomain().getDomain())){
						throw new BusinessException(ALIAS_NOT_IN_DOMAIN);
					}
					alias.setId(new AliasAccountPk(aliasVO.getName(), aliasVO.getDomain()));
					account.getAliases().add(alias);
				}
			}
		}
		
		return account;
	}
	
	@Override
	public Collection<EmailAccountVO> findByDomain(String domainId) {
		return this.emailAccountTranslator.translate(this.emailAccountDao.finbAllByDomainId(domainId));
	}

	@Override
	public Collection<EmailAccountVO> findAll() {
		return this.emailAccountTranslator.translate(this.emailAccountDao.readAll(new Sort("id.account","domain.domain")));
	}

	@Override
	public void insertAccountAlias(String accountAlias, String domainAlias,
			String account, String domain) {
		
		DomainAlias da = domainAliasDao.readByPrimaryKey(domainAlias.trim().toLowerCase());
		if(da==null){
			throw new BusinessException(ALIAS_NOT_EXISTS);
		}
		if(!da.getDomain().getDomain().equalsIgnoreCase(domain)){
			throw new BusinessException(ALIAS_NOT_IN_DOMAIN);
		}
		if(aliasDao.exists(new AliasAccountPk(accountAlias, domainAlias))){
			throw new BusinessException(ALIAS_ALREADY_EXISTS);
		}
		

		EmailAccountPk pk = new EmailAccountPk();
		pk.setAccount(account);
		pk.setDomainId(domain);
		EmailAccount emailAccount = emailAccountDao.readByPrimaryKey(pk);
		
		AliasAccount alias = new AliasAccount();
		alias.setId(new AliasAccountPk(accountAlias, domainAlias));
		alias.setDomainAlias(da);
		alias.setCreated(Calendar.getInstance());
		alias.setAccount(emailAccount);
		alias.setDataSource(EmailAccount.MANUAL);
			
		if(emailAccount.getAliases()==null){
			emailAccount.setAliases(new HashSet<AliasAccount>());
		}
		emailAccount.getAliases().add(alias);
		
		emailAccountDao.save(emailAccount);
	}

	@Override
	public void removeAccountAlias(String accountAlias, String domainAlias) {
		aliasDao.deleteAliasAccountById(accountAlias, domainAlias);
	}

}
