package org.mxhero.webapi.restful.controller.provision;

import org.mxhero.webapi.entity.Connector;
import org.mxhero.webapi.entity.Account;
import org.mxhero.webapi.entity.ResultPage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/domains/{domain}/connector")
public class ConnectorsController {

	@RequestMapping(method = RequestMethod.GET)
	public Connector read(@PathVariable("domain")String domain) {	
		return new Connector();
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseStatus( value=HttpStatus.OK )
	public void delete(@PathVariable("domain") String domain){
	}	
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus( value=HttpStatus.CREATED )
	public void create(@PathVariable("domain")String domain, @RequestBody Connector connector){
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	@ResponseStatus( value=HttpStatus.OK )
	public void update(@PathVariable("domain") String domain, @RequestBody Connector connector){
	}	
	
	@RequestMapping(value="/test", method = RequestMethod.POST)
	public ResultPage<Account> test(@PathVariable("domain")String domain, @RequestBody Connector connector) {	
		return new ResultPage<Account>();
	}
	
	@RequestMapping(value="/refresh", method = RequestMethod.POST)
	@ResponseStatus( value=HttpStatus.OK )
	public void refresh(@PathVariable("domain")String domain) {	
	}

}
