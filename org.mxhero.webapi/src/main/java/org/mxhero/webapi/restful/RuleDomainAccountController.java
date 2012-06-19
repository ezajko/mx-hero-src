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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/domains/{domain}/accounts/{account}/rules")
public class RuleDomainAccountController {

	@Autowired(required=true)
	private RuleService ruleService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain) or (hasRole('ROLE_DOMAIN_ACCOUNT') and #domain == principal.domain and #account = principal.account)")
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<RuleVO> readAll(@PathVariable("domain") String domain, @PathVariable("account") String account, String component){
		return ruleService.readAll(domain,account,component);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain and #ruleVO.domain == principal.domain) or (hasRole('ROLE_DOMAIN_ACCOUNT') and #domain == principal.domain and #account = principal.account)")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public @ResponseBody RuleVO create(@PathVariable("domain") String domain,@PathVariable("account") String account, @RequestBody RuleVO ruleVO){
		if(ruleVO.getDomain()==null 
				|| (!domain.equalsIgnoreCase(ruleVO.getFromDirection().getDomain()) 
					&& !domain.equalsIgnoreCase(ruleVO.getToDirection().getDomain()))
				|| (!account.equalsIgnoreCase(ruleVO.getFromDirection().getAccount()) 
						&& !account.equalsIgnoreCase(ruleVO.getToDirection().getAccount()))){
			throw new NotAllowedException("rule.domain.account.outside");
		}
		return ruleService.create(ruleVO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain) or (hasRole('ROLE_DOMAIN_ACCOUNT') and #domain == principal.domain and #account = principal.account)")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody RuleVO read(@PathVariable("domain") String domain, @PathVariable("account") String account, @PathVariable("id")Long id){
		RuleVO rule = ruleService.read(id);
		if(rule.getDomain()==null 
				|| (!domain.equalsIgnoreCase(rule.getFromDirection().getDomain()) 
					&& !domain.equalsIgnoreCase(rule.getToDirection().getDomain()))
				|| (!account.equalsIgnoreCase(rule.getFromDirection().getAccount()) 
						&& !account.equalsIgnoreCase(rule.getToDirection().getAccount()))){
			throw new UnknownResourceException("rule.not.found");
		}
		return rule;
	}
	
	@PreAuthorize("#id==#ruleVO.id and (hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain ) or (hasRole('ROLE_DOMAIN_ACCOUNT') and #domain == principal.domain and #account = principal.account))")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void update(@PathVariable("domain") String domain, @PathVariable("account") String account, @PathVariable("id")Long id,  @RequestBody RuleVO ruleVO){
		RuleVO rule = ruleService.read(id);
		if(rule.getDomain()==null 
				|| (!domain.equalsIgnoreCase(rule.getFromDirection().getDomain()) 
					&& !domain.equalsIgnoreCase(rule.getToDirection().getDomain()))
				|| (!account.equalsIgnoreCase(rule.getFromDirection().getAccount()) 
						&& !account.equalsIgnoreCase(rule.getToDirection().getAccount()))){
			throw new UnknownResourceException("rule.not.found");
		}
		if(ruleVO.getDomain()==null 
				|| (!domain.equalsIgnoreCase(ruleVO.getFromDirection().getDomain()) 
					&& !domain.equalsIgnoreCase(ruleVO.getToDirection().getDomain()))
				|| (!account.equalsIgnoreCase(ruleVO.getFromDirection().getAccount()) 
						&& !account.equalsIgnoreCase(ruleVO.getToDirection().getAccount()))){
			throw new NotAllowedException("rule.domain.account.outside");
		}
		ruleService.update(ruleVO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain) or (hasRole('ROLE_DOMAIN_ACCOUNT') and #domain == principal.domain and #account = principal.account)")
	@RequestMapping(value = "/{id}/status", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void status(@PathVariable("domain") String domain, @PathVariable("account") String account, @PathVariable("id")Long id,  Boolean enabled){
		RuleVO rule = ruleService.read(id);
		if(rule.getDomain()==null 
				|| (!domain.equalsIgnoreCase(rule.getFromDirection().getDomain()) 
					&& !domain.equalsIgnoreCase(rule.getToDirection().getDomain()))
				|| (!account.equalsIgnoreCase(rule.getFromDirection().getAccount()) 
						&& !account.equalsIgnoreCase(rule.getToDirection().getAccount()))){
			throw new UnknownResourceException("rule.not.found");
		}
		rule.setEnabled(enabled);
		ruleService.update(rule);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain) or (hasRole('ROLE_DOMAIN_ACCOUNT') and #domain == principal.domain and #account = principal.account)")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	public void delete(@PathVariable("domain") String domain, @PathVariable("account") String account, @PathVariable("id")Long id){
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
