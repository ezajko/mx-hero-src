package org.mxhero.webapi.restful;

import org.mxhero.webapi.service.DomainAccountService;
import org.mxhero.webapi.service.GroupService;
import org.mxhero.webapi.service.exception.ConflictResourceException;
import org.mxhero.webapi.service.exception.UnknownResourceException;
import org.mxhero.webapi.vo.AccountVO;
import org.mxhero.webapi.vo.GroupVO;
import org.mxhero.webapi.vo.PageVO;
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
@RequestMapping("/domains/{domain}/groups")
public class DomainGroupController {

	@Autowired(required=true)
	private GroupService groupService;
	
	@Autowired(required=true)
	private DomainAccountService accountService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(method = RequestMethod.GET)
	public PageVO<GroupVO> readAll(@PathVariable("domain")String domain, Integer limit, Integer offset){
		return groupService.readAll(domain, limit, offset);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public GroupVO create(@PathVariable("domain")String domain, @RequestBody GroupVO groupVO){
		if(!domain.equalsIgnoreCase(groupVO.getDomain())){
			throw new ConflictResourceException("domain.group.domain.not.match");
		}
		return groupService.create(groupVO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public GroupVO read(@PathVariable("domain")String domain, @PathVariable("name")String name){
		GroupVO groupVO = groupService.read(domain,name);
		if(!domain.equalsIgnoreCase(groupVO.getDomain())){
			throw new UnknownResourceException("domain.group.not.found");
		}
		return groupVO;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domainVO.domain == principal.domain)")
	@RequestMapping(value = "/{name}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void update(@PathVariable("domain")String domain, @PathVariable("name")String name, @RequestBody GroupVO groupVO){
		if(!domain.equalsIgnoreCase(groupVO.getDomain())){
			throw new ConflictResourceException("domain.group.domain.not.match");
		}
		if(!groupVO.getName().equalsIgnoreCase(name)){
			throw new ConflictResourceException("domain.group.name.not.match");
		}
		groupService.update(groupVO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(value = "/{name}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	public void delete(@PathVariable("domain")String domain, @PathVariable("name")String name){
		groupService.delete(domain, name);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(value = "/accounts/available", method = RequestMethod.GET)
	public PageVO<AccountVO> readAllNoGroupAccounts(@PathVariable("domain")String domain, Integer limit, Integer offset){
		return groupService.readAllNoGroupAccounts(domain, limit, offset);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(value = "/{name}/accounts", method = RequestMethod.GET)
	public PageVO<AccountVO> readAllGroupAccounts(@PathVariable("domain")String domain, @PathVariable("name")String name, Integer limit, Integer offset){
		return groupService.readAllGroupAccounts(domain, name, limit, offset);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(value = "/{name}/accounts/{account}/add", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public AccountVO addAccount(@PathVariable("domain")String domain, @PathVariable("name")String name, @PathVariable("account")String account){
		return groupService.addAccount(domain, name, account);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(value = "/{name}/accounts/{account}/remove", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	public void removeAccount(@PathVariable("domain")String domain, @PathVariable("name")String name, @PathVariable("account")String account){
		groupService.removeAccount(domain, name, account);
	}

	public GroupService getGroupService() {
		return groupService;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public DomainAccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(DomainAccountService accountService) {
		this.accountService = accountService;
	}
	
}
