package org.mxhero.webapi.restful;

import org.mxhero.webapi.service.UserService;
import org.mxhero.webapi.vo.PageVO;
import org.mxhero.webapi.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/users")
public class UserController {
	
	@Autowired(required=true)
	private UserService userService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody PageVO<UserVO> readAll(Integer limit, Integer offset ) {	
		return userService.readAll(null, limit, offset);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public @ResponseBody UserVO create(@RequestBody UserVO user, String role){
		String definedRole = UserVO.ROLE_DOMAIN_ACCOUNT;
		if(user.getDomain()==null){
			definedRole = UserVO.ROLE_ADMIN;
			user.setAccount(null);
		}else{
			if(user.getAccount()==null){
				definedRole = UserVO.ROLE_DOMAIN_ADMIN;
			}else{
				definedRole = UserVO.ROLE_DOMAIN_ACCOUNT;
			}
		}
		return userService.create(user,definedRole);
	}
	
	@PreAuthorize("permitAll")
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void resetPassword(String email){
		userService.resetPassword(email);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{username}", method = RequestMethod.GET)
	public @ResponseBody UserVO read(@PathVariable("username")String username) {	
		return userService.read(username);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{username}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void update(@PathVariable("username") String username, @RequestBody UserVO user){
		userService.update(username, user);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{username}/changePassword", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void changePassword(@PathVariable("username") String username, String oldPassword, String newPassword){
		userService.changePassword(username, oldPassword, newPassword, null);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{username}/setPassword", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void setPassword(@PathVariable("username") String username, String newPassword){
		userService.setPassword(username, newPassword);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{username}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	public void delete(@PathVariable("username") String username){
		userService.delete(username, null);
	}
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
