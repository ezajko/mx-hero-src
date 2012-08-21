package org.mxhero.console.backend.repository;

import java.util.List;

import org.mxhero.console.backend.infrastructure.pagination.common.PageResult;
import org.mxhero.console.backend.vo.AccountPropertyVO;
import org.mxhero.console.backend.vo.EmailAccountAliasVO;
import org.mxhero.console.backend.vo.EmailAccountVO;

public interface EmailAccountRepository {

	void deleteByDomainId(String domainId);
	
	void delete(String account, String domainId);
	
	EmailAccountVO findById(String account, String domain);
	
	void update(EmailAccountVO accountVO);
	
	void insert(EmailAccountVO accountVO);
	
	void insertAlias(EmailAccountAliasVO aliasVO);
	
	void deleteAlias(EmailAccountAliasVO aliasVO);
	
	PageResult<EmailAccountVO> findMembersByGroupId(String domainId, String groupName, int pageNo, int pageSize);
	
	PageResult<EmailAccountVO> findMembersByDomainIdWithoutGroup(String domainId, int pageNo, int pageSize);

	PageResult<EmailAccountVO> findAll(String domainId, String account, String group, int pageNo, int pageSize);
	
	List<AccountPropertyVO> readProperties(String account, String domainId);
	
	void refreshProperties(String account, String domainId, List<AccountPropertyVO> properties);
	
	void deleteProperties(String account, String domainId);
}
