package org.mxhero.webapi.restful;

import java.util.List;

import org.mxhero.webapi.service.DomainAccountService;
import org.mxhero.webapi.service.exception.ConflictResourceException;
import org.mxhero.webapi.vo.AccountPropertiesVO;
import org.mxhero.webapi.vo.AccountPropertyVO;
import org.mxhero.webapi.vo.AccountVO;
import org.mxhero.webapi.vo.PageVO;
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
@RequestMapping("/domains/{domain}/accounts")
public class DomainAccountController {

	@Autowired(required=true)
	private DomainAccountService accountService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody PageVO<AccountVO> readAll(@PathVariable("domain")String domain, Integer limit, Integer offset){
		return accountService.readAll(domain, limit, offset);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public @ResponseBody AccountVO create(@PathVariable("domain")String domain, @RequestBody AccountVO accountVO){
		if(!domain.equalsIgnoreCase(accountVO.getDomain())){
			throw new ConflictResourceException("domain.account.domains.not.match");
		}
		return accountService.create(accountVO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(value = "/{account}", method = RequestMethod.GET)
	public @ResponseBody AccountVO read(@PathVariable("domain")String domain, @PathVariable("account")String account){
		return accountService.read(domain, account);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domainVO.domain == principal.domain)")
	@RequestMapping(value = "/{account}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void update(@PathVariable("domain")String domain, @PathVariable("account")String account, @RequestBody AccountVO accountVO){
		if(!domain.equalsIgnoreCase(accountVO.getDomain())){
			throw new ConflictResourceException("domain.account.domains.not.match");
		}
		if(!account.equalsIgnoreCase(accountVO.getAccount())){
			throw new ConflictResourceException("domain.account.not.match");
		}
		accountService.update(accountVO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(value = "/{account}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	public void delete(@PathVariable("domain")String domain, @PathVariable("account")String account){
		accountService.delete(domain, account);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(value = "/{account}/properties", method = RequestMethod.GET)
	public @ResponseBody List<AccountPropertyVO> readAllAccountProperties(@PathVariable("domain")String domain, @PathVariable("account")String account){
		return accountService.readProperties(domain, account);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(value = "/{account}/properties", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void updateAllAccountProperties(@PathVariable("domain")String domain, @PathVariable("account")String account, @RequestBody AccountPropertiesVO properties){
		accountService.updateProperties(domain, account, properties);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(value = "/{account}/properties", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	public void deleteAllAccountProperties(@PathVariable("domain")String domain, @PathVariable("account")String account){
		accountService.deleteProperties(domain, account);
	}
	
	public DomainAccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(DomainAccountService accountService) {
		this.accountService = accountService;
	}

}
