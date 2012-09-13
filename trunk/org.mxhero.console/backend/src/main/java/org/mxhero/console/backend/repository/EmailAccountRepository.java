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

package org.mxhero.console.backend.repository;

import java.util.List;

import org.mxhero.console.backend.infrastructure.pagination.common.PageResult;
import org.mxhero.console.backend.vo.AccountPropertyVO;
import org.mxhero.console.backend.vo.EmailAccountAliasVO;
import org.mxhero.console.backend.vo.EmailAccountVO;

public interface EmailAccountRepository {

	void deleteByDomainId(String domainId);
	
	void delete(String account, String domainId);
	
	EmailAccountVO findById(String account, String domain);
	
	void update(EmailAccountVO accountVO);
	
	void insert(EmailAccountVO accountVO);
	
	void insertAlias(EmailAccountAliasVO aliasVO);
	
	void deleteAlias(EmailAccountAliasVO aliasVO);
	
	PageResult<EmailAccountVO> findMembersByGroupId(String domainId, String groupName, int pageNo, int pageSize);
	
	PageResult<EmailAccountVO> findMembersByDomainIdWithoutGroup(String domainId, int pageNo, int pageSize);

	PageResult<EmailAccountVO> findAll(String domainId, String account, String group, int pageNo, int pageSize);
	
	List<AccountPropertyVO> readProperties(String account, String domainId);
	
	void refreshProperties(String account, String domainId, List<AccountPropertyVO> properties);
	
	void deleteProperties(String account, String domainId);
}
