package org.mxhero.webapi.restful.controller.provision;

import org.mxhero.webapi.entity.Domain;
import org.mxhero.webapi.entity.ResultPage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/domains")
public class DomainsController {

	@RequestMapping(method = RequestMethod.GET)
	public ResultPage<Domain> readAll(String domain, Integer limit, Integer offset ) {	
		ResultPage<Domain> page = new ResultPage<Domain>(); 
		return page;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus( value=HttpStatus.CREATED )
	public void create(String domain, String server){
	}
	
	@RequestMapping(value="/{domain}", method = RequestMethod.GET)
	public Domain read(@PathVariable("domain")String domain) {	
		return new Domain();
	}
	
	@RequestMapping(value="/{domain}", method = RequestMethod.PUT)
	@ResponseStatus( value=HttpStatus.OK )
	public void update(@PathVariable("domain") String domain, String server){
	}	
	
	@RequestMapping(value="/{domain}", method = RequestMethod.DELETE)
	@ResponseStatus( value=HttpStatus.OK )
	public void delete(@PathVariable("domain") String domain){
	}	
	
	@RequestMapping(value="/{domain}/aliases", method = RequestMethod.GET)
	public ResultPage<String> readAliases(@PathVariable("domain")String domain){
		return new ResultPage<String>();
	}
	
	@RequestMapping(value="/{domain}/aliases", method = RequestMethod.POST)
	@ResponseStatus( value=HttpStatus.CREATED )
	public void createAlias(@PathVariable("domain")String domain, String alias){
	}
	
	@RequestMapping(value="/{domain}/aliases/{alias}", method = RequestMethod.DELETE)
	@ResponseStatus( value=HttpStatus.OK )
	public void deleteAlias(@PathVariable("domain") String domain, @PathVariable("alias") String alias){
	}

}
