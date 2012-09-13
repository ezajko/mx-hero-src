/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
