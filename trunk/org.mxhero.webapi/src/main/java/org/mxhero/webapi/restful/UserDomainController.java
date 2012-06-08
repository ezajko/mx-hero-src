package org.mxhero.webapi.restful;

import org.mxhero.webapi.service.UserService;
import org.mxhero.webapi.service.exception.UnknownResourceException;
import org.mxhero.webapi.vo.PageVO;
import org.mxhero.webapi.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/domains/{domain}/users")
public class UserDomainController {

	@Autowired(required=true)
	private UserService userService;

	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(method = RequestMethod.GET)
	public PageVO<UserVO> readAll(@PathVariable("domain") String domain, Integer limit, Integer offset ) {	
		return userService.readAll(null, limit, offset);
	}
	
	@PreAuthorize("(hasRole('ROLE_ADMIN') and #domain == user.domain) or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain and #user.domain == principal.domain and #user.account!=null)")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public UserVO create(@PathVariable("domain") String domain, @RequestBody UserVO user){
		return userService.create(user,UserVO.ROLE_DOMAIN_ACCOUNT);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@PostAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and returnObject.domain == principal.domain)")
	@RequestMapping(value = "/{username}", method = RequestMethod.GET)
	public UserVO read(@PathVariable("domain") String domain, @PathVariable("username")String username) {	
		return userService.read(username);
	}
	
	@PreAuthorize("(hasRole('ROLE_ADMIN') and #domain == #user.domain) or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain and #user.domain == principal.domain)")
	@RequestMapping(value = "/{username}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void update(@PathVariable("domain") String domain, @PathVariable("username") String username, @RequestBody UserVO user){
		userService.update(username, user);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(value = "/{username}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	public void delete(@PathVariable("domain") String domain, @PathVariable("username") String username){
		userService.delete(username, domain);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(value = "/{username}/changePassword", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void changePassword(@PathVariable("domain") String domain, @PathVariable("username") String username, String oldPassword, String newPassword){
		userService.changePassword(username, oldPassword, newPassword, domain);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(value = "/{username}/setPassword", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void setPassword(@PathVariable("domain") String domain, @PathVariable("username") String username, String newPassword){
		UserVO user = userService.read(username);
		if(user==null){
			throw new UnknownResourceException("user.not.found");
		}
		if(user.getDomain()==null || !user.getDomain().equalsIgnoreCase(domain)){
			throw new UnknownResourceException("user.not.found");
		}
		userService.setPassword(username, newPassword);
	}
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
