package org.mxhero.console.backend.service.jdbc;

import java.util.ArrayList;
import java.util.Collection;

import org.mxhero.console.backend.infrastructure.BusinessException;
import org.mxhero.console.backend.repository.EmailAccountRepository;
import org.mxhero.console.backend.repository.FeatureRuleRepository;
import org.mxhero.console.backend.service.EmailAccountService;
import org.mxhero.console.backend.vo.EmailAccountAliasVO;
import org.mxhero.console.backend.vo.EmailAccountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("emailAccountService")
@RemotingDestination(channels={"flex-amf"})
public class JdbcEmailAccountService implements EmailAccountService{

	public static final String EMAIL_ALREADY_EXISTS="email.already.exists";
	public static final String ALIAS_ALREADY_EXISTS="alias.account.already.exists";
	
	private EmailAccountRepository accountRepository;
	private FeatureRuleRepository ruleRepository;

	@Autowired(required=true)
	public JdbcEmailAccountService(EmailAccountRepository accountRepository,
			FeatureRuleRepository ruleRepository) {
		this.accountRepository = accountRepository;
		this.ruleRepository = ruleRepository;
	}

	public Collection<EmailAccountVO> findPageBySpecs(String domainId, String email, String group) {
		return accountRepository.findAll(domainId, email, group);
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void remove(String account,String domain) {
		ruleRepository.deleteByAccount(domain, account);
		accountRepository.delete(account, domain);
	}

	@Override
	public void edit(EmailAccountVO emailAccountVO) {
		accountRepository.update(emailAccountVO);
	}
	
	@Override
	public void insert(EmailAccountVO emailAccountVO, String domainId) {
		try{
			accountRepository.insert(emailAccountVO);
		} catch (DataIntegrityViolationException e){
			throw new BusinessException(EMAIL_ALREADY_EXISTS);
		}
		
	}

	@Override
	public Collection<EmailAccountVO> upload(Collection<EmailAccountVO> emailAccountVOs, String domainId,
			Boolean failOnError) {
		Collection<EmailAccountVO> errorEmailAccountsVOs = new ArrayList<EmailAccountVO>();

		if(failOnError){
			uploadTransaction(emailAccountVOs);
		}else{
			for(EmailAccountVO emailAccountVO : emailAccountVOs){
				try{
					accountRepository.insert(emailAccountVO);
				}catch(Exception e){
					errorEmailAccountsVOs.add(emailAccountVO);
				}
			}	
		}
		return errorEmailAccountsVOs;
	}
	
	@Transactional(value="mxhero",readOnly=false)
	public void uploadTransaction(Collection<EmailAccountVO> emailAccountVOs){
		for(EmailAccountVO emailAccountVO : emailAccountVOs){
			accountRepository.insert(emailAccountVO);
		}
	}
	
	@Override
	public Collection<EmailAccountVO> findByDomain(String domainId) {
		return accountRepository.findAll(domainId, null, null);
	}

	@Override
	public Collection<EmailAccountVO> findAll() {
		return accountRepository.findAll(null, null, null);
	}

	@Override
	public void insertAccountAlias(String accountAlias, String domainAlias,
			String account, String domain) {
		EmailAccountAliasVO aliasVO = new EmailAccountAliasVO();
		aliasVO.setDataSource(EmailAccountVO.MANUAL);
		aliasVO.setDomain(domainAlias);
		aliasVO.setName(accountAlias);
		aliasVO.setAccount(new EmailAccountVO());
		aliasVO.getAccount().setAccount(account);
		aliasVO.getAccount().setDomain(domain);
		try{
			accountRepository.insertAlias(aliasVO);
		} catch (DataIntegrityViolationException e){
			throw new BusinessException(ALIAS_ALREADY_EXISTS);
		}
	}

	@Override
	public void removeAccountAlias(String accountAlias, String domainAlias) {
		EmailAccountAliasVO aliasVO = new EmailAccountAliasVO();
		aliasVO.setDataSource(EmailAccountVO.MANUAL);
		aliasVO.setDomain(domainAlias);
		aliasVO.setName(accountAlias);
		accountRepository.deleteAlias(aliasVO);
	}

}
