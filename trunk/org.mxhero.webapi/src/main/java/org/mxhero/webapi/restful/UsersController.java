package org.mxhero.webapi.restful;

import java.util.Set;

import org.mxhero.webapi.service.UserService;
import org.mxhero.webapi.vo.PageVO;
import org.mxhero.webapi.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/users")
public class UsersController {
	
	@Autowired(required=true)
	private UserService userService;

	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain = principal.domain)")
	@RequestMapping(method = RequestMethod.GET)
	public PageVO<UserVO> readAll(String domain, Integer limit, Integer offset ) {	
		return userService.readAll(domain, limit, offset);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_ADMIN') and #user.domain == principal.domain)")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public UserVO create(@RequestBody UserVO user){
		String definedRole = UserVO.ROLE_DOMAIN_ACCOUNT;
		Set<String> authorities = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
		if(authorities.contains(UserVO.ROLE_ADMIN)){
			if(user.getDomain()==null){
				definedRole = UserVO.ROLE_ADMIN;
			}else{
				if(user.getAccount()==null){
					definedRole = UserVO.ROLE_DOMAIN_ADMIN;
				}else{
					definedRole = UserVO.ROLE_DOMAIN_ACCOUNT;
				}
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
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DOMAIN_ADMIN') or (hasRole('ROLE_DOMAIN_ACCOUNT') and #username == principal.username)")
	@PostAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and returnObject.domain == principal.domain) or (hasRole('ROLE_DOMAIN_ACCOUNT') and #username == principal.username)")
	@RequestMapping(value = "/{username}", method = RequestMethod.GET)
	public UserVO read(@PathVariable("username")String username) {	
		return userService.read(username);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #user.domain == principal.domain) or (hasRole('ROLE_DOMAIN_ACCOUNT') and #username == principal.username and #user.username == principal.username)")
	@RequestMapping(value = "/{username}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void update(@PathVariable("username") String username, @RequestBody UserVO user){
		userService.update(username, user);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain) or (hasRole('ROLE_DOMAIN_ACCOUNT') and #username == principal.username and #domain == principal.domain)")
	@RequestMapping(value = "/{username}/changePassword", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void changePassword(@PathVariable("username") String username, String oldPassword, String newPassword, String domain){
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain) ")
	@RequestMapping(value = "/{username}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	public void delete(@PathVariable("username") String username, String domain){
	}
	
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
