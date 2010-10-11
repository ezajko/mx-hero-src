package org.mxhero.console.backend.service;

import java.util.Collection;

import org.mxhero.console.backend.entity.ApplicationUser;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ApplicationUserService {

	@Secured("ROLE_ADMIN")
	void changeUserPassword(String username, String password, String newPassword);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	void changePassword(String newPassword);
	
	@Transactional(readOnly=true)
	@Secured("ROLE_ADMIN")
	Collection<ApplicationUser> findAll();
	
	@Transactional(readOnly=true)
	@Secured("ROLE_ADMIN")
	ApplicationUser findByUserName(String userName);

	@Secured("ROLE_DOMAIN_ADMIN")
	void update(ApplicationUser applicationUser);
	
	@Transactional(readOnly=true)
	@Secured("ROLE_DOMAIN_ADMIN")
	public ApplicationUser getUser();

}
