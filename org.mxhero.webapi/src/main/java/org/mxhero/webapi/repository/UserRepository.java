package org.mxhero.webapi.repository;

import org.mxhero.webapi.infrastructure.pagination.common.PageResult;
import org.mxhero.webapi.vo.UserVO;

public interface UserRepository {
	
	PageResult<UserVO> findall(String domain, Integer pageNo, Integer pageSize);
	
	void update(UserVO applicationUser);
	
	UserVO insert(UserVO applicationUser, String rol, String password);

	void delete(String  username);
	
	UserVO finbByNotifyEmail(String email);

	UserVO finbByUserName(String userName);
	
	UserVO finbByAccount(String domain, String account);
	
	boolean changePassword(String oldPassword, String newPassword, String username);
	
	boolean setPassword(String newPassword, String username);
	
}
