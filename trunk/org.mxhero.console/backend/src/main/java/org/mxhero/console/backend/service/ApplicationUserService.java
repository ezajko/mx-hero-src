package org.mxhero.console.backend.service;

import org.mxhero.console.backend.vo.ApplicationUserVO;

public interface ApplicationUserService {

	void changePassword(String oldPassword, String newPassword);
	
	void changePassword(String oldPassword, String newPassword, Integer id);
	
	ApplicationUserVO edit(ApplicationUserVO applicationUser);
	
	public ApplicationUserVO getUser();

	public boolean isAuthenticated();
	
	public void sendPassword(String email);

}
