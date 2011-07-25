package org.mxhero.console.backend.service;

import java.util.Collection;

import org.mxhero.console.backend.vo.EmailAccountVO;
import org.mxhero.console.backend.vo.GroupVO;
import org.springframework.security.access.annotation.Secured;

public interface GroupService {

	@Secured("ROLE_DOMAIN_ADMIN")
	Collection<GroupVO> findAll(String domainId);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	Collection<EmailAccountVO> findMembersByGroupId(String groupId, String domainId);

	@Secured("ROLE_DOMAIN_ADMIN")
	Collection<EmailAccountVO> findMembersByDomainIdWithoutGroup(String domainId);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	void remove(String groupName, String domainId);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	void insert(GroupVO groupVO, Collection<EmailAccountVO> members);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	void edit(GroupVO groupVO, Collection<EmailAccountVO> members);
}
