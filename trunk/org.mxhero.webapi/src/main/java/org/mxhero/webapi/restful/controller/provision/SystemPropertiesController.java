package org.mxhero.webapi.restful.controller.provision;

import org.mxhero.webapi.entity.ResultPage;
import org.mxhero.webapi.entity.Property;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/systemProperties")
public class SystemPropertiesController {


	@RequestMapping(method = RequestMethod.GET)
	public ResultPage<Property> readAll(Integer limit, Integer offset){
		return new ResultPage<Property>();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus( value=HttpStatus.CREATED )
	public void create(String key, String value){
		
	}
	
	@RequestMapping(value="/{key}", method = RequestMethod.GET)
	public Property read(@PathVariable("key")String key) {	
		return new Property();
	}
	
	@RequestMapping(value="/{key}", method = RequestMethod.PUT)
	@ResponseStatus( value=HttpStatus.OK )
	public void update(@PathVariable("key") String key, String value){
	}	
	
	@RequestMapping(value="/{key}", method = RequestMethod.DELETE)
	@ResponseStatus( value=HttpStatus.OK )
	public void delete(@PathVariable("key") String key){
	}	
	
}
