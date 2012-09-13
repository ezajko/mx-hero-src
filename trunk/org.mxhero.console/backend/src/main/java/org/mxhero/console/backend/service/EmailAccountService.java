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
import java.util.List;

import org.mxhero.console.backend.vo.AccountPropertyVO;
import org.mxhero.console.backend.vo.EmailAccountVO;
import org.mxhero.console.backend.vo.PageVO;

public interface EmailAccountService {

	PageVO findPageBySpecs(String domainId, String email,  String group, int pageNo, int pageSize);
	
	void remove(String account, String domain);
	
	void insert(EmailAccountVO emailAccountVO,String domainId);
	
	void edit(EmailAccountVO emailAccountVO);
	
	void insertAccountAlias(String accountAlias, String domainAlias, String account, String domain);
	
	void removeAccountAlias(String accountAlias, String domainAlias);
	
	Collection<EmailAccountVO> upload(Collection<EmailAccountVO> emailAccountVOs, String domainId, Boolean failOnError);
	
	public void updateProperties(String domain, String account, List<AccountPropertyVO> properties);
	
	public List<AccountPropertyVO> readProperties(String domain, String account);
}
