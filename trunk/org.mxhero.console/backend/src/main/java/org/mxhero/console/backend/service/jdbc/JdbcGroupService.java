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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("groupService")
@RemotingDestination(channels={"flex-amf"})
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
	public Collection<EmailAccountVO> findMembersByGroupId(String groupId, String domainId) {
		return accountRepository.findMembersByGroupId(domainId, groupId);
	}

	@Override
	public Collection<EmailAccountVO> findMembersByDomainIdWithoutGroup(
			String domainId) {
		return accountRepository.findMembersByDomainIdWithoutGroup(domainId);
	}

	@Override
	public Collection<GroupVO> findAll(String domainId) {
		return groupRepository.findByDomain(domainId);
	}

	@Override
	@Transactional(readOnly=false)
	public void remove(String groupName, String domainId) {
		ruleRepository.deleteByGroup(domainId, groupName);
		groupRepository.delete(groupName,domainId);
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void insert(GroupVO groupVO, Collection<EmailAccountVO> members) {
		try{
			groupRepository.insert(groupVO);
		} catch (DataIntegrityViolationException e){
			throw new BusinessException(GROUP_ALREADY_EXISTS);
		}
		if(members!=null && members.size()>0){
			for(EmailAccountVO account : members){
				account.setGroup(groupVO.getName());
				account.setUpdatedDate(Calendar.getInstance());
				accountRepository.update(account);
			}
		}
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void edit(GroupVO groupVO, Collection<EmailAccountVO> members) {
		groupRepository.update(groupVO);
		groupRepository.releaseMembers(groupVO.getName(), groupVO.getDomain());
		if(members!=null && members.size()>0){
			for(EmailAccountVO account : members){
				account.setGroup(groupVO.getName());
				account.setUpdatedDate(Calendar.getInstance());
				accountRepository.update(account);
			}
		}
	}

}
