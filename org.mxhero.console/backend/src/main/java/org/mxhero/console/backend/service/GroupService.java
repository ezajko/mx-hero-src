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
