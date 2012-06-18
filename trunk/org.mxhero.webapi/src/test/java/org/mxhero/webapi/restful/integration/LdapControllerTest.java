package org.mxhero.webapi.restful.integration;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.webapi.Utils;
import org.mxhero.webapi.service.DomainService;
import org.mxhero.webapi.service.LdapService;
import org.mxhero.webapi.service.UserService;
import org.mxhero.webapi.service.exception.UnknownResourceException;
import org.mxhero.webapi.vo.DomainVO;
import org.mxhero.webapi.vo.LdapVO;
import org.mxhero.webapi.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = {"file:src/test/resources/infrastructure-context.xml","file:src/test/resources/admin-test-context.xml"})
public class LdapControllerTest {

	@Autowired
	private UserService userService;
	@Autowired
	private DomainService domainService;
	@Autowired
	private LdapService ldapService;
	@Autowired
	private RestTemplate template;
	private String base = "http://localhost:8080/webapi/api/v1/domains/{domain}/adldap";
	private String type = ".json";
	private String domainName = "test.com";
	
	@Before
	public void init(){
		Utils.setUser(Utils.getUser("admin"), UserVO.ROLE_ADMIN, userService);
		DomainVO domain = new DomainVO();
		domain.setDomain(domainName);
		domain.setServer("smtp.server");
		try{domainService.delete(domainName);}catch(UnknownResourceException e){}
		domainService.create(domain);
	}
	
	@After
	public void clean(){
		try{domainService.delete(domainName);}catch(UnknownResourceException e){}
	}
	
	@Test
	public void create(){
		String url = (base+type).replace("{domain}", domainName);
		try{ldapService.delete(domainName);}catch(UnknownResourceException e){}
		LdapVO ldap = new LdapVO();
		ldap.setDomain(domainName);
		template.postForObject(url, ldap, Object.class);
		ldapService.delete(domainName);
	}
	
	@Test
	public void read(){
		String url = (base+type).replace("{domain}", domainName);
		try{ldapService.delete(domainName);}catch(UnknownResourceException e){}
		LdapVO ldap = new LdapVO();
		ldap.setDomain(domainName);
		ldapService.create(ldap);
		template.getForObject(url, Object.class);
		ldapService.delete(domainName);
	}

	@Test
	public void update(){
		String url = (base+type).replace("{domain}", domainName);
		try{ldapService.delete(domainName);}catch(UnknownResourceException e){}
		LdapVO ldap = new LdapVO();
		ldap.setDomain(domainName);
		ldapService.create(ldap);
		ldap = ldapService.read(ldap.getDomain());
		ldap.setAddres("TEST");
		template.put(url, ldap);
		Assert.assertTrue(ldapService.read(ldap.getDomain()).getAddres().equalsIgnoreCase(ldap.getAddres()));
		ldapService.delete(domainName);
	}

	@Test
	public void delete(){
		String url = (base+type).replace("{domain}", domainName);
		try{ldapService.delete(domainName);}catch(UnknownResourceException e){}
		LdapVO ldap = new LdapVO();
		ldap.setDomain(domainName);
		ldapService.create(ldap);
		template.delete(url);
	}
	
}
