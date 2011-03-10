package org.mxhero.console.backend.service.jpa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.mxhero.console.backend.dao.DomainDao;
import org.mxhero.console.backend.dao.EmailAccountDao;
import org.mxhero.console.backend.dao.FeatureRuleDao;
import org.mxhero.console.backend.dao.GroupDao;
import org.mxhero.console.backend.entity.EmailAccount;
import org.mxhero.console.backend.entity.FeatureRule;
import org.mxhero.console.backend.infrastructure.BusinessException;
import org.mxhero.console.backend.infrastructure.PipedSpecifications;
import org.mxhero.console.backend.service.EmailAccountService;
import org.mxhero.console.backend.service.FeatureService;
import org.mxhero.console.backend.translator.EmailAccountTranslator;
import org.mxhero.console.backend.vo.EmailAccountVO;
import org.mxhero.console.backend.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;
import org.synyx.hades.domain.Page;
import org.synyx.hades.domain.PageRequest;
import org.synyx.hades.domain.Sort;

import static org.mxhero.console.backend.infrastructure.PipedSpecifications.*;
import static org.mxhero.console.backend.dao.specs.EmailAccountSpecs.*;


@Service("emailAccountService")
@RemotingDestination(channels={"flex-amf"})
public class JpaEmailAccountService implements EmailAccountService{

	public static final String EMAIL_ALREADY_EXISTS="email.already.exists";
	public static final String EMAIL_INVALID_FORMAT="email.invalid.format";
	
	private EmailAccountDao emailAccountDao;
	
	private DomainDao domainDao;
	
	private EmailAccountTranslator emailAccountTranslator;
	
	private GroupDao groupDao;
	
	private FeatureRuleDao featureRuleDao;
	
	private FeatureService featureService;
	
	@Autowired
	public JpaEmailAccountService(EmailAccountDao emailAccountDao,
			DomainDao domainDao, EmailAccountTranslator emailAccountTranslator,
			GroupDao groupDao,
			FeatureRuleDao featureRuleDao,
			FeatureService featureService) {
		this.emailAccountDao = emailAccountDao;
		this.domainDao = domainDao;
		this.emailAccountTranslator = emailAccountTranslator;
		this.groupDao = groupDao;
		this.featureRuleDao = featureRuleDao;
		this.featureService = featureService;
	}

	public PageVO<EmailAccountVO> findPageBySpecs(Integer domainId, String email, String name, String lastName, Integer groupId, Integer page, Integer pageSize) {

		PipedSpecifications<EmailAccount> specifications = where(domainIdEqual(domainId));

		if(email!=null && !email.trim().isEmpty()){
			specifications.and(accountLike(email));
		}
		if(name!=null && !name.trim().isEmpty()){
			specifications.and(nameLike(name));
		}
		if(lastName!=null && !lastName.trim().isEmpty()){
			specifications.and(lastNameLike(lastName));
		}
		if(groupId!=null && groupId>-1){
			specifications.and(groupIdEqual(groupId));
		}
	
		PageRequest pr = new PageRequest(page, pageSize);
		Page<EmailAccount> pageData = emailAccountDao.readAll(specifications, pr);
		return emailAccountTranslator.translate(pageData);

	}

	@Override
	public void remove(Integer emailId) {
		EmailAccount emailAccount= emailAccountDao.readByPrimaryKey(emailId);
		
		if(emailAccount!=null){
			for(FeatureRule rule : featureRuleDao.findByDirectionTypeAndValueId("individual",emailAccount.getId())){
				featureService.remove(rule.getId());
			}
			
			emailAccountDao.delete(emailAccount);
		}
	}

	@Override
	public void edit(EmailAccountVO emailAccountVO) {
		EmailAccount emailAccount =emailAccountDao.readByPrimaryKey(emailAccountVO.getId());
		emailAccount.setName(emailAccountVO.getName());
		emailAccount.setLastName(emailAccountVO.getLastName());
		emailAccount.setUpdatedDate(Calendar.getInstance());
		if(emailAccountVO.getGroupId()==null || emailAccountVO.getGroupId()<0){
			emailAccount.setGroup(null);
		} else {
			emailAccount.setGroup(groupDao.readByPrimaryKey(emailAccountVO.getGroupId()));
		}
		emailAccountDao.save(emailAccount);
	}

	@Override
	public void insert(EmailAccountVO emailAccountVO, Integer domainId) {

		EmailAccount newEmailAccount = new EmailAccount();
		newEmailAccount.setUpdatedDate(Calendar.getInstance());
		newEmailAccount.setCreatedDate(Calendar.getInstance());
		newEmailAccount.setAccount(emailAccountVO.getAccount().toLowerCase());
		newEmailAccount.setLastName(emailAccountVO.getLastName());
		newEmailAccount.setName(emailAccountVO.getName());
		newEmailAccount.setDomain(domainDao.readByPrimaryKey(domainId));
		if(emailAccountVO.getGroupId()==null || emailAccountVO.getGroupId()<0){
			newEmailAccount.setGroup(null);
		} else {
			newEmailAccount.setGroup(groupDao.readByPrimaryKey(emailAccountVO.getGroupId()));
		}
		try{
			emailAccountDao.save(newEmailAccount);
		} catch (DataIntegrityViolationException e){
			throw new BusinessException(EMAIL_ALREADY_EXISTS);
		}
	}

	@Override
	public Collection<EmailAccountVO> upload(Collection<EmailAccountVO> emailAccountVOs, Integer domainId,
			Boolean failOnError) {
		
		Collection<EmailAccount> newEmailAccounts = new ArrayList<EmailAccount>(emailAccountVOs.size());;
		Collection<EmailAccountVO> errorEmailAccountsVOs = new ArrayList<EmailAccountVO>();
		
		for(EmailAccountVO emailAccountVO : emailAccountVOs){
			EmailAccount newEmail= new EmailAccount();
			if(emailAccountVO.getAccount()==null){
				
			}
			newEmail.setAccount(emailAccountVO.getAccount().toLowerCase());
			newEmail.setCreatedDate(Calendar.getInstance());
			newEmail.setDomain(domainDao.readByPrimaryKey(domainId));
			newEmail.setLastName(emailAccountVO.getLastName());
			newEmail.setName(emailAccountVO.getName());
			newEmail.setUpdatedDate(Calendar.getInstance());
			newEmailAccounts.add(newEmail);
		}
		
		if(failOnError){
			try{
				emailAccountDao.save(newEmailAccounts);
			}catch(DataIntegrityViolationException e){
				throw new BusinessException(EMAIL_ALREADY_EXISTS);
			}catch(ConstraintViolationException e){
				throw new BusinessException(EMAIL_INVALID_FORMAT);
			}
		}else{
			for(EmailAccount emailAccount : newEmailAccounts){
				try{
					emailAccountDao.save(emailAccount);
				}catch(DataIntegrityViolationException e){
					EmailAccountVO errorEmailAccountVO = new EmailAccountVO();
					errorEmailAccountVO.setAccount(emailAccount.getAccount().toLowerCase());
					errorEmailAccountVO.setName(emailAccount.getLastName());
					errorEmailAccountVO.setLastName(emailAccount.getLastName());
					errorEmailAccountsVOs.add(errorEmailAccountVO);
				}catch(ConstraintViolationException e){
					EmailAccountVO errorEmailAccountVO = new EmailAccountVO();
					errorEmailAccountVO.setAccount(emailAccount.getAccount().toLowerCase());
					errorEmailAccountVO.setName(emailAccount.getLastName());
					errorEmailAccountVO.setLastName(emailAccount.getLastName());
					errorEmailAccountsVOs.add(errorEmailAccountVO);
				}
			}	
		}
		return errorEmailAccountsVOs;
	}

	@Override
	public Collection<EmailAccountVO> findByDomain(Integer domainId) {
		return this.emailAccountTranslator.translate(this.emailAccountDao.finbAllByDomainId(domainId));
	}

	@Override
	public Collection<EmailAccountVO> findAll() {
		return this.emailAccountTranslator.translate(this.emailAccountDao.readAll(new Sort("account","domain.domain")));
	}

}
