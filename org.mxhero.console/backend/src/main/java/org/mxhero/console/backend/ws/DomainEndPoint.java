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

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.mxhero.console.backend.service.DomainService;
import org.mxhero.console.backend.vo.DomainAdLdapVO;
import org.mxhero.console.backend.vo.DomainVO;
import org.mxhero.console.backend.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@WebService
public class DomainEndPoint {

	private DomainService domainService;
	
	@Autowired
	public DomainEndPoint(@Qualifier("jdbcDomainService")DomainService domainService) {
		this.domainService = domainService;
	}
	
	@WebMethod(action="createDomain")
	public void insert(
			@WebParam(name="domainVO")DomainVO domainVO, 
			@WebParam(name="hasAdmin")Boolean hasAdmin, 
			@WebParam(name="password")String password, 
			@WebParam(name="email")String email) {
		domainService.insert(domainVO, hasAdmin, password, email);	
	}
	
	@WebMethod(action="editDomain")
	public void edit(
			@WebParam(name="domainVO")DomainVO domainVO, 
			@WebParam(name="hasAdmin")Boolean hasAdmin, 
			@WebParam(name="password")String password, 
			@WebParam(name="email")String email) {
		domainService.edit(domainVO, hasAdmin, password, email);
	}
	
	@WebMethod(action="findAll")
	public PageVO findAll(
			@WebParam(name="domainId")String domainId, 
			@WebParam(name="pageNo")int pageNo, 
			@WebParam(name="pageSize")int pageSize) {
		return domainService.findAll(domainId, pageNo, pageSize);
	}
	
	@WebMethod(action="deleteDomain")
	public void delete(@WebParam(name="domainId")String domainId){
		domainService.remove(domainId);
	}
	
	@WebMethod(action="insertAdLdap")
	public DomainAdLdapVO insertAdLdap(@WebParam(name="adLdapVO")DomainAdLdapVO adLdapVO){
		return domainService.insertAdLdap(adLdapVO);
	}

	@WebMethod(action="editAdLdap")
	public DomainAdLdapVO editAdLdap(@WebParam(name="adLdapVO")DomainAdLdapVO adLdapVO){
		return domainService.editAdLdap(adLdapVO);
	}
	
	@WebMethod(action="deleteAdLdap")
	public void deleteAdLdap(@WebParam(name="domainId")String domainId){
		domainService.removeAdLdap(domainId);
	}

	@WebMethod(action="refreshAdLdap")
	public DomainAdLdapVO refreshAdLdap(@WebParam(name="domainId")String domainId){
		return domainService.refreshAdLdap(domainId);
	}
}
