package org.mxhero.console.backend.service;

import java.util.Collection;

import org.mxhero.console.backend.vo.EmailAccountVO;
import org.mxhero.console.backend.vo.GroupVO;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface GroupService {

	@Secured("ROLE_DOMAIN_ADMIN")
	Collection<GroupVO> findAll(Integer domainId);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	Collection<EmailAccountVO> findMembersByGroupId(Integer groupId);

	@Secured("ROLE_DOMAIN_ADMIN")
	Collection<EmailAccountVO> findMembersByDomainIdWithoutGroup(Integer domainId);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	void remove(Integer groupId);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	void insert(GroupVO groupVO, Integer domainId, Collection<EmailAccountVO> members);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	void edit(GroupVO groupVO, Collection<EmailAccountVO> members);
}
