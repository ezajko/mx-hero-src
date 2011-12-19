package org.mxhero.console.backend.repository;

import org.mxhero.console.backend.infrastructure.pagination.common.PageResult;
import org.mxhero.console.backend.vo.GroupVO;

public interface GroupRepository {

	void deleteByDomainId(String domainId);
	
	PageResult<GroupVO> findByDomain(String domainId, int pageNo, int pageSize);
	
	void insert(GroupVO group);
	
	void update(GroupVO group);
	
	void delete(String group, String domainId);
	
	void releaseMembers(String group, String domainId);
}
