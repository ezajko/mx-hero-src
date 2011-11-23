package org.mxhero.console.backend.repository;

import java.util.List;

import org.mxhero.console.backend.vo.GroupVO;

public interface GroupRepository {

	void deleteByDomainId(String domainId);
	
	List<GroupVO> findByDomain(String domainId);
	
	void insert(GroupVO group);
	
	void update(GroupVO group);
	
	void delete(String group, String domainId);
	
	void releaseMembers(String group, String domainId);
}
