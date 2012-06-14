package org.mxhero.webapi.repository;

import org.mxhero.webapi.infrastructure.pagination.common.PageResult;
import org.mxhero.webapi.vo.GroupVO;

public interface GroupRepository {

	void deleteByDomainId(String domainId);
	
	PageResult<GroupVO> findByDomain(String domainId, Integer pageNo, Integer pageSize);
	
	GroupVO find(String domain, String name);
	
	void insert(GroupVO group);
	
	void update(GroupVO group);
	
	void delete(String group, String domainId);
	
	void releaseMembers(String group, String domainId);
}
