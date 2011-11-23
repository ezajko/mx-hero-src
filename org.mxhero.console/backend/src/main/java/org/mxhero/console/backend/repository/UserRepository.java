package org.mxhero.console.backend.repository;

import java.util.List;

import org.mxhero.console.backend.vo.ApplicationUserVO;
import org.mxhero.console.backend.vo.AuthorityVO;

public interface UserRepository {
	
	void update(ApplicationUserVO applicationUserVO);
	
	ApplicationUserVO insert(ApplicationUserVO applicationUserVO, String password);

	void delete(Integer id);
	
	ApplicationUserVO finbByNotifyEmail(String email);

	ApplicationUserVO finbByUserName(String userName);
	
	ApplicationUserVO finbById(Integer id);
	
	boolean changePassword(String oldPassword, String newPassword, Integer id);
	
	void setPassword(String newPassword, Integer id);
	
	List<AuthorityVO> findAllAuthorities();

}
