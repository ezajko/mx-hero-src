package org.mxhero.console.backend.service;

import org.mxhero.console.backend.vo.ApplicationUserVO;
import org.springframework.security.access.annotation.Secured;

public interface ApplicationUserService {

	@Secured("ROLE_DOMAIN_ADMIN")
	void changePassword(String oldPassword, String newPassword);
	
	@Secured("ROLE_ADMIN")
	void changePassword(String oldPassword, String newPassword, Integer id);

	@Secured("ROLE_DOMAIN_ADMIN")
	ApplicationUserVO edit(ApplicationUserVO applicationUser);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	public ApplicationUserVO getUser();

	public void sendPassword(String email);

}
