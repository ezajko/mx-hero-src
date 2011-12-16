package org.mxhero.console.backend.repository;

import java.util.List;

import org.mxhero.console.backend.infrastructure.pagination.common.PageResult;
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
	
	List<EmailAccountVO> findMembersByGroupId(String domainId, String groupName);
	
	List<EmailAccountVO> findMembersByDomainIdWithoutGroup(String domainId);

	PageResult<EmailAccountVO> findAll(String domainId, String account, String group, int pageNo, int pageSize);
}
