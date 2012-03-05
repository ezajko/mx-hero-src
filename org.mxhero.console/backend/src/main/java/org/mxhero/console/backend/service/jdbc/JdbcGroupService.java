package org.mxhero.console.backend.service.jdbc;

import java.util.Calendar;
import java.util.Collection;

import org.mxhero.console.backend.infrastructure.BusinessException;
import org.mxhero.console.backend.repository.EmailAccountRepository;
import org.mxhero.console.backend.repository.FeatureRuleRepository;
import org.mxhero.console.backend.repository.GroupRepository;
import org.mxhero.console.backend.service.GroupService;
import org.mxhero.console.backend.vo.EmailAccountVO;
import org.mxhero.console.backend.vo.GroupVO;
import org.mxhero.console.backend.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("jdbcGroupService")
public class JdbcGroupService implements GroupService{

	public static final String GROUP_ALREADY_EXISTS="group.already.exists";
	
	private EmailAccountRepository accountRepository;
	
	private GroupRepository groupRepository;
	
	private FeatureRuleRepository ruleRepository;
	
	@Autowired
	public JdbcGroupService(EmailAccountRepository accountRepository,
			GroupRepository groupRepository,
			FeatureRuleRepository ruleRepository) {
		super();
		this.accountRepository = accountRepository;
		this.groupRepository = groupRepository;
		this.ruleRepository = ruleRepository;
	}

	@Override
	public PageVO findMembersByGroupId(String groupId, String domainId, int pageNo, int pageSize) {
		return accountRepository.findMembersByGroupId(domainId, groupId, pageNo, pageSize).createVO();
	}

	@Override
	public PageVO findMembersByDomainIdWithoutGroup(String domainId, int pageNo, int pageSize) {
		return accountRepository.findMembersByDomainIdWithoutGroup(domainId, pageNo, pageSize).createVO();
	}

	@Override
	public PageVO findAll(String domainId, int pageNo, int pageSize) {
		return groupRepository.findByDomain(domainId,pageNo,pageSize).createVO();
	}

	@Override
	@Transactional(readOnly=false)
	public void remove(String groupName, String domainId) {
		ruleRepository.deleteByGroup(domainId, groupName);
		groupRepository.delete(groupName,domainId);
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void insert(GroupVO groupVO) {
		try{
			groupRepository.insert(groupVO);
		} catch (DataIntegrityViolationException e){
			throw new BusinessException(GROUP_ALREADY_EXISTS);
		}
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void edit(GroupVO groupVO) {
		groupRepository.update(groupVO);
	}

	@Override
	public void insertGroupMember(GroupVO groupVO, Collection<EmailAccountVO> accounts) {
		for(EmailAccountVO account : accounts){
			account.setGroup(groupVO.getName());
			account.setUpdatedDate(Calendar.getInstance());
			accountRepository.update(account);
		}
	}

	@Override
	public void removeGroupMember(Collection<EmailAccountVO> accounts) {
		for(EmailAccountVO account : accounts){
			account.setGroup(null);
			account.setUpdatedDate(Calendar.getInstance());
			accountRepository.update(account);
		}
	}

}
