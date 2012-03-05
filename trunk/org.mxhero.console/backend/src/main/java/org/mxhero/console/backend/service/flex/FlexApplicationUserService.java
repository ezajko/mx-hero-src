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
