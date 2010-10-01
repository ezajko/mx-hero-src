package org.mxhero.console.backend.service;

import java.util.Collection;

import org.mxhero.console.backend.entity.ApplicationUser;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ApplicationUserService {

	void changePassword(String username, String password, String newPassword);
	
	@Transactional(readOnly=true)
	Collection<ApplicationUser> findAll();
	
	@Transactional(readOnly=true)
	ApplicationUser findByUserName(String userName);
	
	void update(ApplicationUser applicationUser);
}
