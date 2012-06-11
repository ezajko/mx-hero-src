package org.mxhero.webapi.restful;

import java.util.List;

import org.mxhero.webapi.service.RuleService;
import org.mxhero.webapi.service.exception.NotAllowedException;
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
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/domains/{domain}/rules")
public class RuleDomainController {

	@Autowired(required=true)
	private RuleService ruleService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(method = RequestMethod.GET)
	public List<RuleVO> readAll(@PathVariable("domain") String domain, String component){
		return ruleService.readAll(domain,null,component);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain and #ruleVO.domain == principal.domain)")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public RuleVO create(@PathVariable("domain") String domain, @RequestBody RuleVO ruleVO){
		if(ruleVO.getDomain()==null 
				|| (!domain.equalsIgnoreCase(ruleVO.getFromDirection().getDomain()) 
					&& !domain.equalsIgnoreCase(ruleVO.getToDirection().getDomain()))){
			throw new NotAllowedException("rule.domain.only");
		}
		return ruleService.create(ruleVO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public RuleVO read(@PathVariable("domain") String domain, @PathVariable("id")Long id){
		RuleVO rule = ruleService.read(id);
		if(rule.getDomain()!=null){
			throw new UnknownResourceException("rule.not.found");
		}
		return rule;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain and #ruleVO.domain == principal.domain)")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void update(@PathVariable("domain") String domain, @PathVariable("id")Long id,  @RequestBody RuleVO ruleVO){
		RuleVO rule = ruleService.read(id);
		if(rule.getDomain()!=null || ruleVO.getDomain()!=null){
			throw new UnknownResourceException("rule.not.found");
		}
		if(ruleVO.getDomain()==null 
				|| (!domain.equalsIgnoreCase(ruleVO.getFromDirection().getDomain()) 
					&& !domain.equalsIgnoreCase(ruleVO.getToDirection().getDomain()))){
			throw new NotAllowedException("rule.domain.only");
		}
		ruleService.update(ruleVO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(value = "/{id}/status", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void status(@PathVariable("domain") String domain, @PathVariable("id")Long id,  Boolean enabled){
		RuleVO rule = ruleService.read(id);
		if(domain.equalsIgnoreCase(rule.getDomain())){
			throw new UnknownResourceException("rule.not.found");
		}
		rule.setEnabled(enabled);
		ruleService.update(rule);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	public void delete(@PathVariable("domain") String domain, @PathVariable("id")Long id){
		RuleVO rule = ruleService.read(id);
		if(!domain.equalsIgnoreCase(rule.getDomain())){
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
