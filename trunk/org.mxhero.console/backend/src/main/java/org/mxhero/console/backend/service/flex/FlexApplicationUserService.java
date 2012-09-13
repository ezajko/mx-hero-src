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

import org.mxhero.console.backend.service.ApplicationUserService;
import org.mxhero.console.backend.vo.ApplicationUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service("applicationUserService")
@RemotingDestination(channels={"flex-amf"})
public class FlexApplicationUserService implements ApplicationUserService {

	private ApplicationUserService service;

	@Autowired(required=true)
	public FlexApplicationUserService(@Qualifier("jdbcApplicationUserService")ApplicationUserService service) {
		this.service = service;
	}

	@Secured("ROLE_DOMAIN_ADMIN")
	public void changePassword(String oldPassword, String newPassword) {
		this.service.changePassword(oldPassword, newPassword);
	}

	@Secured("ROLE_ADMIN")
	public void changePassword(String oldPassword, String newPassword,
			Integer id) {
		this.service.changePassword(oldPassword, newPassword, id);
	}

	@Secured("ROLE_DOMAIN_ADMIN")
	public ApplicationUserVO edit(ApplicationUserVO applicationUser) {
		return this.service.edit(applicationUser);
	}

	@Secured("ROLE_DOMAIN_ADMIN")
	public ApplicationUserVO getUser() {
		return this.service.getUser();
	}

	public boolean isAuthenticated() {
		return this.service.isAuthenticated();
	}

	public void sendPassword(String email) {
		this.service.sendPassword(email);
	}

}
