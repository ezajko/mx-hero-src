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

import org.mxhero.console.backend.service.DomainService;
import org.mxhero.console.backend.vo.DomainAdLdapVO;
import org.mxhero.console.backend.vo.DomainVO;
import org.mxhero.console.backend.vo.LdapAccountVO;
import org.mxhero.console.backend.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service("domainService")
@RemotingDestination(channels={"flex-amf"})
public class FlexDomainService implements DomainService{

	private DomainService service;

	@Autowired(required=true)
	public FlexDomainService(@Qualifier("jdbcDomainService")DomainService service) {
		this.service = service;
	}

	@Override
	@Secured("ROLE_ADMIN")
	public PageVO findAll(String domainName, int pageNo, int pageSize) {
		return service.findAll(domainName, pageNo, pageSize);
	}

	@Override
	@Secured("ROLE_ADMIN")
	public void remove(String domain) {
		this.service.remove(domain);
	}

	@Override
	@Secured("ROLE_ADMIN")
	public void insert(DomainVO domainVO, Boolean hasAdmin, String password,
			String email) {
		this.service.insert(domainVO, hasAdmin, password, email);
	}

	@Override
	@Secured("ROLE_ADMIN")
	public void edit(DomainVO domainVO, Boolean hasAdmin, String password,
			String email) {
		this.service.edit(domainVO, hasAdmin, password, email);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public DomainAdLdapVO insertAdLdap(DomainAdLdapVO adLdapVO) {
		return this.service.insertAdLdap(adLdapVO);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public DomainAdLdapVO editAdLdap(DomainAdLdapVO adLdapVO) {
		return this.service.editAdLdap(adLdapVO);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public void removeAdLdap(String domainId) {
		this.service.removeAdLdap(domainId);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public DomainAdLdapVO refreshAdLdap(String domainId) {
		return this.service.refreshAdLdap(domainId);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public Collection<LdapAccountVO> testAdLdap(DomainAdLdapVO adLdapVO) {
		return this.service.testAdLdap(adLdapVO);
	}

}
