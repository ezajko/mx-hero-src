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

package org.mxhero.console.backend.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.mxhero.console.backend.service.EmailAccountService;
import org.mxhero.console.backend.vo.AccountPropertyVO;
import org.mxhero.console.backend.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@WebService
public class AccountEndPoint {

	private EmailAccountService accountService;

	@Autowired(required=true)
	public AccountEndPoint(@Qualifier("jdbcEmailAccountService")EmailAccountService accountService) {
		this.accountService = accountService;
	}
	
	@WebMethod(action="readAccounts")
	PageVO findPageBySpecs(@WebParam(name="domainId")String domainId, 
							@WebParam(name="email")String email, 
							@WebParam(name="pageNo")int pageNo, 
							@WebParam(name="pageSize")int pageSize){
		return accountService.findPageBySpecs(domainId, email, null, pageNo, pageSize);
	}
	
	@WebMethod(action="updateProperties")
	public void updateProperties(@WebParam(name="domainId")String domainId, 
								@WebParam(name="account")String account, 
								@WebParam(name="properties")List<AccountPropertyVO> properties) {
		accountService.updateProperties(domainId, account, properties);
	}

	@WebMethod(action="updateProperties")
	public List<AccountPropertyVO> readProperties(@WebParam(name="domainId")String domainId, 
													@WebParam(name="account")String account) {
		return accountService.readProperties(domainId, account);
	}
}
