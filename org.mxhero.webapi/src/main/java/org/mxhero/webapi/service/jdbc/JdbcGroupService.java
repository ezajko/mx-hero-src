package org.mxhero.webapi.service.jdbc;

import org.mxhero.webapi.infrastructure.pagination.common.PageResult;
import org.mxhero.webapi.repository.AccountRepository;
import org.mxhero.webapi.repository.GroupRepository;
import org.mxhero.webapi.service.GroupService;
import org.mxhero.webapi.service.exception.ConflictResourceException;
import org.mxhero.webapi.service.exception.UnknownResourceException;
import org.mxhero.webapi.vo.AccountVO;
import org.mxhero.webapi.vo.GroupVO;
import org.mxhero.webapi.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("jdbcGroupService")
public class JdbcGroupService implements GroupService{

	private GroupRepository groupRepository;
	private AccountRepository accountRepository;
	
	@Autowired
	public JdbcGroupService(GroupRepository groupRepository, AccountRepository accountRepository) {
		this.groupRepository = groupRepository;
		this.accountRepository = accountRepository;
	}

	@Override
	public PageVO<GroupVO> readAll(String domain, Integer limit, Integer offset) {
		PageVO<GroupVO> groupVOPage = new PageVO<GroupVO>();
		PageResult<GroupVO> result = groupRepository.findByDomain(domain, offset, limit);
		groupVOPage.setActualPage(result.getPageNumber());
		groupVOPage.setTotalElements(result.getTotalRecordsNumber());
		groupVOPage.setTotalPages(result.getPageAmount());
		groupVOPage.setElements(result.getPageData());
		return groupVOPage;
	}

	@Override
	public GroupVO create(GroupVO groupVO) {
		groupRepository.insert(groupVO);
		GroupVO insertedGroup = groupRepository.find(groupVO.getDomain(), groupVO.getName());
		if(insertedGroup==null){
			throw new UnknownResourceException("domain.group.not.found");
		}
		return insertedGroup;
	}

	@Override
	public GroupVO read(String domain, String name) {
		GroupVO group = groupRepository.find(domain, name);
		if(group==null){
			throw new UnknownResourceException("domain.group.not.found");
		}
		return group;
	}

	@Override
	public void update(GroupVO groupVO) {
		GroupVO group = groupRepository.find(groupVO.getDomain(), groupVO.getName());
		if(group==null){
			throw new UnknownResourceException("domain.group.not.found");
		}
		groupRepository.update(groupVO);
	}

	@Override
	public void delete(String domain, String name) {
		GroupVO group = groupRepository.find(domain, name);
		if(group==null){
			throw new UnknownResourceException("domain.group.not.found");
		}
		groupRepository.delete(name, domain);
	}

	@Override
	public PageVO<AccountVO> readAllNoGroupAccounts(String domain, Integer limit, Integer offset) {
		PageVO<AccountVO> accountVOPage = new PageVO<AccountVO>();
		PageResult<AccountVO> result = accountRepository.findMembersByDomainIdWithoutGroup(domain, offset, limit);
		accountVOPage.setActualPage(result.getPageNumber());
		accountVOPage.setTotalElements(result.getTotalRecordsNumber());
		accountVOPage.setTotalPages(result.getPageAmount());
		accountVOPage.setElements(result.getPageData());
		return accountVOPage;
	}

	@Override
	public PageVO<AccountVO> readAllGroupAccounts(String domain, String name, Integer limit, Integer offset) {
		PageVO<AccountVO> accountVOPage = new PageVO<AccountVO>();
		PageResult<AccountVO> result = accountRepository.findMembersByGroupId(domain, name, offset, limit);
		accountVOPage.setActualPage(result.getPageNumber());
		accountVOPage.setTotalElements(result.getTotalRecordsNumber());
		accountVOPage.setTotalPages(result.getPageAmount());
		accountVOPage.setElements(result.getPageData());
		return accountVOPage;
	}

	@Override
	public void addAccount(String domain, String name,
			String account) {
		AccountVO accountVO = accountRepository.findById(account, domain);
		if(accountVO==null){
			throw new UnknownResourceException("domain.account.not.found");
		}
		if(accountVO.getGroup()!=null){
			throw new ConflictResourceException("domain.group.account.already.has.group");
		}
		accountVO.setGroup(name);
		accountRepository.update(accountVO);
	}

	@Override
	public void removeAccount(String domain, String name,
			String account) {
		AccountVO accountVO = accountRepository.findById(account, domain);
		if(accountVO==null){
			throw new UnknownResourceException("domain.account.not.found");
		}
		if(name.equalsIgnoreCase(accountVO.getGroup())){
			throw new ConflictResourceException("domain.group.account.not.in.group");
		}
		accountVO.setGroup(null);
		accountRepository.update(accountVO);
	}

}
