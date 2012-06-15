package org.mxhero.webapi;

import java.util.Calendar;

import org.mxhero.webapi.service.UserService;
import org.mxhero.webapi.vo.UserVO;

public abstract class Utils {

	public static final String PASSWORD = "password";
	
	public static void setUser(UserVO user, String role, UserService userService){
		UserVO userInBase = userService.read(user.getUserName());
		if(userInBase!=null){
			userService.setPassword(user.getUserName(), PASSWORD);
		}else{
			userService.create(user, role);
		}
	}
	
	public static UserVO getUser(String username){
		UserVO user = new UserVO();
		user.setCreated(Calendar.getInstance());
		user.setLastName(username);
		user.setName(username);
		user.setUserName(username);
		user.setPassword(PASSWORD);
		return user;
	}
	
	
}
