package org.mxhero.console.backend.service;

import java.util.Collection;

import org.mxhero.console.backend.vo.EmailAccountVO;
import org.mxhero.console.backend.vo.GroupVO;
import org.mxhero.console.backend.vo.PageVO;
import org.springframework.security.access.annotation.Secured;

public interface GroupService {

	@Secured("ROLE_DOMAIN_ADMIN")
	PageVO findAll(String domainId, int pageNo, int pageSize);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	PageVO findMembersByGroupId(String groupId, String domainId, int pageNo, int pageSize);

	@Secured("ROLE_DOMAIN_ADMIN")
	PageVO findMembersByDomainIdWithoutGroup(String domainId, int pageNo, int pageSize);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	void remove(String groupName, String domainId);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	void insert(GroupVO groupVO);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	void edit(GroupVO groupVO);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	void insertGroupMember(GroupVO groupVO, Collection<EmailAccountVO> accounts);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	void removeGroupMember(Collection<EmailAccountVO> accounts);
}
