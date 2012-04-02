package org.mxhero.webapi.restful.controller.provision;

import org.mxhero.webapi.entity.ApplicationUser;
import org.mxhero.webapi.entity.ResultPage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/users")
public class UsersController {

	
	@RequestMapping(method = RequestMethod.GET)
	public ResultPage<ApplicationUser> readAll(String domain, Integer limit, Integer offset ) {	
		ResultPage<ApplicationUser> page = new ResultPage<ApplicationUser>(); 
		return page;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus( value=HttpStatus.CREATED )
	public void create(@RequestBody ApplicationUser user){
	}
	
	@RequestMapping(value="/resetPassword", method = RequestMethod.POST)
	@ResponseStatus( value=HttpStatus.OK )
	public void resetPassword(String email){
	}
	
	@RequestMapping(value="/{username}", method = RequestMethod.GET)
	public ApplicationUser read(@PathVariable("username")String username) {	
		return new ApplicationUser();
	}
	
	@RequestMapping(value="/{username}", method = RequestMethod.PUT)
	@ResponseStatus( value=HttpStatus.OK )
	public void update(@PathVariable("username") String username, String server){
	}
	
	@RequestMapping(value="/{username}", method = RequestMethod.DELETE)
	@ResponseStatus( value=HttpStatus.OK )
	public void delete(@PathVariable("username") String username){
	}
	
	@RequestMapping(value="/{username}/changePassword", method = RequestMethod.PUT)
	@ResponseStatus( value=HttpStatus.OK )
	public void changePassword(@PathVariable("username") String username, String oldPassword, String newPassword){
	}
	
}
