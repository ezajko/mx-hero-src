package org.mxhero.webapi.restful.controller.provision;

import org.mxhero.webapi.entity.Account;
import org.mxhero.webapi.entity.ResultPage;
import org.mxhero.webapi.entity.UploadAccounts;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/domains/{domain}/accounts")
public class AccountsController {

	@RequestMapping(method = RequestMethod.GET)
	public ResultPage<Account> readAll(@PathVariable("domain")String domain, String account, String group, Integer limit, Integer offset ) {	
		ResultPage<Account> page = new ResultPage<Account>(); 
		return page;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus( value=HttpStatus.CREATED )
	public void create(@PathVariable("domain")String domain, String account, String group){
	}
	
	@RequestMapping(value="/upload", method = RequestMethod.POST)
	@ResponseStatus( value=HttpStatus.OK )
	public void upload(@PathVariable("domain")String domain, Boolean failOnError, @RequestBody UploadAccounts accounts){
	}
	
	@RequestMapping(value="/{account}", method = RequestMethod.GET)
	public Account read(@PathVariable("domain")String domain, @PathVariable("account")String account) {	
		return new Account();
	}
	
	@RequestMapping(value="/{account}", method = RequestMethod.PUT)
	@ResponseStatus( value=HttpStatus.OK )
	public void update(@PathVariable("domain")String domain, @PathVariable("account") String account, String group){
	}
	
	@RequestMapping(value="/{account}", method = RequestMethod.DELETE)
	@ResponseStatus( value=HttpStatus.OK )
	public void delete(@PathVariable("domain")String domain, @PathVariable("account") String account){
	}

	@RequestMapping(value="/{account}/aliases", method = RequestMethod.GET)
	public ResultPage<String> readAliases(@PathVariable("domain")String domain, @PathVariable("account")String account){
		return new ResultPage<String>();
	}
	
	@RequestMapping(value="/{account}/aliases", method = RequestMethod.POST)
	@ResponseStatus( value=HttpStatus.CREATED )
	public void createAlias(@PathVariable("domain")String domain, @PathVariable("account")String account, String alias){
	}
	
	@RequestMapping(value="/{account}/aliases/{alias}", method = RequestMethod.DELETE)
	@ResponseStatus( value=HttpStatus.OK )
	public void deleteAlias(@PathVariable("domain")String domain, @PathVariable("account") String account, @PathVariable("alias") String alias){
	}
	
}
