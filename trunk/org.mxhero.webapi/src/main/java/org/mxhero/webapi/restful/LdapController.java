package org.mxhero.webapi.restful;

import org.mxhero.webapi.service.LdapService;
import org.mxhero.webapi.vo.LdapVO;
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
@RequestMapping("/domains/{domain}/adldap")
public class LdapController {

	@Autowired(required=true)
	private LdapService ldapService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain and #ldapVO.domain == principal.domain)")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public LdapVO create(@PathVariable("domain")String domain, @RequestBody LdapVO ldapVO){
		return ldapService.create(ldapVO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(method = RequestMethod.GET)
	public LdapVO read(@PathVariable("domain")String domain){
		return ldapService.read(domain);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain and #ldapVO.domain == principal.domain)")
	@RequestMapping(method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void update(@PathVariable("domain")String domain,  @RequestBody LdapVO ldapVO){
		ldapService.update(ldapVO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_DOMAIN_ADMIN') and #domain == principal.domain)")
	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	public void delete(@PathVariable("domain")String domain){
		ldapService.delete(domain);
	}

	public LdapService getLdapService() {
		return ldapService;
	}

	public void setLdapService(LdapService ldapService) {
		this.ldapService = ldapService;
	}

}
