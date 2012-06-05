package org.mxhero.webapi.restful;


import java.util.List;

import org.mxhero.webapi.service.SystemPropertiesService;
import org.mxhero.webapi.vo.SystemPropertyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/system/properties")
public class SystemPropertiesController {
	
	@Autowired(required=true)
	private SystemPropertiesService systemPropertiesService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.GET)
	public List<SystemPropertyVO> readAll(){
		return systemPropertiesService.readAll();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public SystemPropertyVO create(@PathVariable("key")String key, String value){
		return systemPropertiesService.create(key, value);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{key}", method = RequestMethod.GET)
	public SystemPropertyVO read(@PathVariable("key")String key){
		return systemPropertiesService.read(key);	
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{key}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void update(@PathVariable("key")String key, String value){
		systemPropertiesService.update(key, value);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{key}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	public void delete(@PathVariable("key")String key){
		systemPropertiesService.delete(key);
	}

	public SystemPropertiesService getSystemPropertiesService() {
		return systemPropertiesService;
	}

	public void setSystemPropertiesService(SystemPropertiesService systemPropertiesService) {
		this.systemPropertiesService = systemPropertiesService;
	}
	
}
