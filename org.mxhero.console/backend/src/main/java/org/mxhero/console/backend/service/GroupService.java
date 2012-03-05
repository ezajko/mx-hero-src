package org.mxhero.console.backend.service;

import java.util.Collection;

import org.mxhero.console.backend.vo.EmailAccountVO;
import org.mxhero.console.backend.vo.GroupVO;
import org.mxhero.console.backend.vo.PageVO;

public interface GroupService {

	PageVO findAll(String domainId, int pageNo, int pageSize);
	
	PageVO findMembersByGroupId(String groupId, String domainId, int pageNo, int pageSize);

	PageVO findMembersByDomainIdWithoutGroup(String domainId, int pageNo, int pageSize);
	
	void remove(String groupName, String domainId);
	
	void insert(GroupVO groupVO);

	void edit(GroupVO groupVO);

	void insertGroupMember(GroupVO groupVO, Collection<EmailAccountVO> accounts);

	void removeGroupMember(Collection<EmailAccountVO> accounts);
}
