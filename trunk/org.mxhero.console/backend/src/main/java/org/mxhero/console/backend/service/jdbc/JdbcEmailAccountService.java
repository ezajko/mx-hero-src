/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mxhero.console.backend.service.jdbc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.mxhero.console.backend.infrastructure.BusinessException;
import org.mxhero.console.backend.repository.EmailAccountRepository;
import org.mxhero.console.backend.repository.FeatureRuleRepository;
import org.mxhero.console.backend.service.EmailAccountService;
import org.mxhero.console.backend.vo.AccountPropertyVO;
import org.mxhero.console.backend.vo.EmailAccountAliasVO;
import org.mxhero.console.backend.vo.EmailAccountVO;
import org.mxhero.console.backend.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("jdbcEmailAccountService")
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

	public PageVO findPageBySpecs(String domainId, String email, String group, int pageNo, int pageSize) {
		return accountRepository.findAll(domainId, email, group, pageNo, pageSize).createVO();
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
	
	@Override
	public void updateProperties(String domain, String account, List<AccountPropertyVO> properties) {
		accountRepository.refreshProperties(account, domain, properties);
	}

	@Override
	public List<AccountPropertyVO> readProperties(String domain, String account) {
		return accountRepository.readProperties(account, domain);
	}

}
