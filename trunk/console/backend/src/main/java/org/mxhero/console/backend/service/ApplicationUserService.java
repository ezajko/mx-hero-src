package org.mxhero.console.backend.service;

import java.util.Collection;

import org.mxhero.console.backend.vo.ApplicationUserVO;
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
	Collection<ApplicationUserVO> findAll();
	
	@Transactional(readOnly=true)
	@Secured("ROLE_ADMIN")
	ApplicationUserVO findByUserName(String userName);

	@Secured("ROLE_DOMAIN_ADMIN")
	void update(ApplicationUserVO applicationUser);
	
	@Transactional(readOnly=true)
	@Secured("ROLE_DOMAIN_ADMIN")
	public ApplicationUserVO getUser();

	@Transactional(readOnly=true)
	public void sendPassword(String email);

}
