package org.mxhero.webapi.service;

import org.mxhero.webapi.vo.PageVO;
import org.mxhero.webapi.vo.UserVO;

public interface UserService {
	
	public PageVO<UserVO> readAll(String domain, Integer limit, Integer offset );
	
	public UserVO create(UserVO user, String role);
	
	public void resetPassword(String email);
	
	public UserVO read(String username);
	
	public void update(String username, UserVO user);
	
	public void changePassword(String username, String oldPassword, String newPassword, String domain);
	
	public void delete(String username, String domain);
	
	public void setPassword(String username, String newPassword);
	
}
