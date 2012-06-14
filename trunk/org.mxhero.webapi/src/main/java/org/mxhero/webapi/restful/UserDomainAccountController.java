package org.mxhero.webapi.restful;

import org.mxhero.webapi.service.UserService;
import org.mxhero.webapi.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/domains/{domain}/accounts/{account}/user")
public class UserDomainAccountController {

	@Autowired(required=true)
	private UserService userService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain) or (hasRole('ROLE_DOMAIN_ACCOUNT') and #domain == principal.domain and #account = principal.account)")
	@PostAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and returnObject.domain == principal.domain) or (hasRole('ROLE_DOMAIN_ACCOUNT') and returnObject.domain == principal.domain and returnObject.account = principal.account)")
	@RequestMapping(value = "/{username}", method = RequestMethod.GET)
	public UserVO read(@PathVariable("domain") String domain, @PathVariable("account")String account) {	
		return userService.readByAccount(domain, account);
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
}
