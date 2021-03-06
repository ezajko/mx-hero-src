package org.mxhero.webapi.restful;

import java.util.List;

import org.mxhero.webapi.service.RuleService;
import org.mxhero.webapi.service.exception.UnknownResourceException;
import org.mxhero.webapi.vo.RuleVO;
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
@RequestMapping("/rules")
public class RuleController {

	@Autowired(required=true)
	private RuleService ruleService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<RuleVO> readAll(String component){
		return ruleService.readAll(null,null,component);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public @ResponseBody RuleVO create(@RequestBody RuleVO ruleVO){
		if(ruleVO.getDomain()!=null){
			throw new IllegalArgumentException("rule.domain.admin.only");
		}
		return ruleService.create(ruleVO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody RuleVO read(@PathVariable("id")Long id){
		RuleVO rule = ruleService.read(id);
		if(rule.getDomain()!=null){
			throw new UnknownResourceException("rule.not.found");
		}
		return rule;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void update(@PathVariable("id")Long id,  @RequestBody RuleVO ruleVO){
		RuleVO rule = ruleService.read(id);
		if(rule.getDomain()!=null || ruleVO.getDomain()!=null){
			throw new IllegalArgumentException("rule.domain.admin.only");
		}
		ruleService.update(ruleVO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{id}/status", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void status(@PathVariable("id")Long id,  Boolean enabled){
		RuleVO rule = ruleService.read(id);
		if(rule.getDomain()!=null){
			throw new IllegalArgumentException("rule.domain.admin.only");
		}
		rule.setEnabled(enabled);
		ruleService.update(rule);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	public void delete(@PathVariable("id")Long id){
		RuleVO rule = ruleService.read(id);
		if(rule.getDomain()!=null){
			throw new UnknownResourceException("rule.not.found");
		}
		ruleService.delete(id);
	}
	
	public RuleService getRuleService() {
		return ruleService;
	}

	public void setRuleService(RuleService ruleService) {
		this.ruleService = ruleService;
	}

}
