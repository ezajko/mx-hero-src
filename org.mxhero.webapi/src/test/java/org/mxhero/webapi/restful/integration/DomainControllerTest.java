package org.mxhero.webapi.restful.integration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.webapi.Utils;
import org.mxhero.webapi.service.DomainService;
import org.mxhero.webapi.service.UserService;
import org.mxhero.webapi.service.exception.UnknownResourceException;
import org.mxhero.webapi.vo.DomainVO;
import org.mxhero.webapi.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = {"file:src/test/resources/infrastructure-context.xml","file:src/test/resources/admin-test-context.xml"})
public class DomainControllerTest {
	
	@Autowired
	private UserService userService;
	@Autowired
	private DomainService domainService;
	@Autowired
	private RestTemplate template;
	private String base = "http://localhost:8080/webapi/api/v1/domains";
	private String type = ".json";
	private String domainName = "test.com";
	
	@Before
	public void init(){
		Utils.setUser(Utils.getUser("admin"), UserVO.ROLE_ADMIN, userService);
	}
	
	@Test
	public void readAll(){
		template.getForEntity(base+type, Object.class);
	}
	
	@Test
	public void create(){
		try{domainService.delete(domainName);}catch(UnknownResourceException e){}
		DomainVO domain = new DomainVO();
		domain.setDomain(domainName);
		domain.setServer("smtp.server");
		template.postForEntity(base+type, domain, Object.class);
		domainService.delete(domainName);
	}
	
	@Test
	public void read(){
		try{domainService.delete(domainName);}catch(UnknownResourceException e){}
		DomainVO domain = new DomainVO();
		domain.setDomain(domainName);
		domain.setServer("smtp.server");
		domainService.create(domain);
		template.getForObject(base+"/"+domainName+type,Object.class);
		domainService.delete(domainName);
	}

	@Test
	public void update(){
		try{domainService.delete(domainName);}catch(UnknownResourceException e){}
		DomainVO domain = new DomainVO();
		domain.setDomain(domainName);
		domain.setServer("smtp.server");
		domainService.create(domain);
		domain=domainService.read(domain.getDomain());
		domain.setServer("TEST");
		template.put(base+"/"+domainName+type,domain);
		Assert.assertTrue(domain.getServer().equals(domainService.read(domain.getDomain()).getServer()));
		domainService.delete(domainName);
	}

	@Test
	public void delete(){
		try{domainService.delete(domainName);}catch(UnknownResourceException e){}
		DomainVO domain = new DomainVO();
		domain.setDomain(domainName);
		domain.setServer("smtp.server");
		domainService.create(domain);
		template.delete(base+"/"+domainName+type,domain);
	}
	
}
