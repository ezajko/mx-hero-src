package org.mxhero.webapi.repository;

import java.util.List;

import org.mxhero.webapi.infrastructure.pagination.common.PageResult;
import org.mxhero.webapi.vo.AccountAliasVO;
import org.mxhero.webapi.vo.AccountPropertyVO;
import org.mxhero.webapi.vo.AccountVO;

public interface AccountRepository {

	void deleteByDomainId(String domain);
	
	void delete(String account, String domain);
	
	AccountVO findById(String account, String domain);
	
	void update(AccountVO accountVO);
	
	void insert(AccountVO accountVO);
	
	void insertAlias(String domain, String account, AccountAliasVO aliasVO);
	
	void deleteAlias(AccountAliasVO aliasVO);
	
	PageResult<AccountVO> findMembersByGroupId(String domain, String groupName, Integer pageNo, Integer pageSize);
	
	PageResult<AccountVO> findMembersByDomainIdWithoutGroup(String domain, Integer pageNo, Integer pageSize);

	PageResult<AccountVO> findAll(String domain, String account, String group, Integer pageNo, Integer pageSize);
	
	List<AccountPropertyVO> readProperties(String account, String domainId);
	
	void refreshProperties(String account, String domainId, List<AccountPropertyVO> properties);
	
	void deleteProperties(String account, String domainId);
	
}
