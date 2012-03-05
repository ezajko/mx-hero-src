package org.mxhero.console.backend.service.flex;

import java.util.Collection;

import org.mxhero.console.backend.service.GroupService;
import org.mxhero.console.backend.vo.EmailAccountVO;
import org.mxhero.console.backend.vo.GroupVO;
import org.mxhero.console.backend.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service("groupService")
@RemotingDestination(channels={"flex-amf"})
public class FlexGroupService implements GroupService{

	private GroupService service;
	
	@Autowired(required=true)
	public FlexGroupService(@Qualifier("jdbcGroupService")GroupService service) {
		this.service = service;
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public PageVO findAll(String domainId, int pageNo, int pageSize) {
		return service.findAll(domainId, pageNo, pageSize);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public PageVO findMembersByGroupId(String groupId, String domainId,
			int pageNo, int pageSize) {
		return service.findMembersByGroupId(groupId, domainId, pageNo, pageSize);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public PageVO findMembersByDomainIdWithoutGroup(String domainId,
			int pageNo, int pageSize) {
		return service.findMembersByDomainIdWithoutGroup(domainId, pageNo, pageSize);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public void remove(String groupName, String domainId) {
		service.remove(groupName, domainId);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public void insert(GroupVO groupVO) {
		service.insert(groupVO);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public void edit(GroupVO groupVO) {
		service.edit(groupVO);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public void insertGroupMember(GroupVO groupVO,
			Collection<EmailAccountVO> accounts) {
		service.insertGroupMember(groupVO, accounts);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public void removeGroupMember(Collection<EmailAccountVO> accounts) {
		service.removeGroupMember(accounts);
	}

}
