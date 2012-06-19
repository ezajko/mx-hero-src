package org.mxhero.webapi.restful.integration;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.webapi.Utils;
import org.mxhero.webapi.service.DomainAccountService;
import org.mxhero.webapi.service.DomainService;
import org.mxhero.webapi.service.UserService;
import org.mxhero.webapi.service.exception.UnknownResourceException;
import org.mxhero.webapi.vo.AccountAliasVO;
import org.mxhero.webapi.vo.AccountPropertiesVO;
import org.mxhero.webapi.vo.AccountPropertyVO;
import org.mxhero.webapi.vo.AccountVO;
import org.mxhero.webapi.vo.DomainVO;
import org.mxhero.webapi.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = {"file:src/test/resources/infrastructure-context.xml","file:src/test/resources/admin-test-context.xml"})
public class DomainAccountControllerTest {

	@Autowired
	private UserService userService;
	@Autowired
	private DomainService domainService;
	@Autowired
	private DomainAccountService accountService;
	@Autowired
	private RestTemplate template;
	private String base = "http://localhost:8080/webapi/api/v1/domains/{domain}/accounts";
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
	public void readAll(){
		String url = (base+type).replace("{domain}", domainName);
		template.getForObject(url, Object.class);
	}
	
	@Test
	public void create(){
		String url = (base+type).replace("{domain}", domainName);
		String accountName = "jhon";
		try{accountService.delete(domainName,accountName);}catch(UnknownResourceException e){}
		AccountVO accountVO = new AccountVO();
		accountVO.setDomain(domainName);
		accountVO.setAccount(accountName);
		try{accountService.delete(domainName,accountName);}catch(UnknownResourceException e){}
		template.postForObject(url, accountVO, Object.class);
		accountService.delete(domainName,accountName);
	}
	
	@Test
	public void read(){
		String url = (base+type).replace("{domain}", domainName);
		String accountName = "jhon";
		try{accountService.delete(domainName,accountName);}catch(UnknownResourceException e){}
		AccountVO accountVO = new AccountVO();
		accountVO.setDomain(domainName);
		accountVO.setAccount(accountName);
		try{accountService.delete(domainName,accountName);}catch(UnknownResourceException e){}
		accountService.create(accountVO);
		template.getForObject(url, Object.class);
		accountService.delete(domainName,accountName);
	}
	
	@Test
	public void update(){
		String accountName = "jhon";
		String url = (base+"/"+accountName+type).replace("{domain}", domainName);
		try{accountService.delete(domainName,accountName);}catch(UnknownResourceException e){}
		AccountVO accountVO = new AccountVO();
		accountVO.setDomain(domainName);
		accountVO.setAccount(accountName);
		try{accountService.delete(domainName,accountName);}catch(UnknownResourceException e){}
		accountService.create(accountVO);
		accountVO = accountService.read(domainName,accountName);
		AccountAliasVO aliasVO = new AccountAliasVO();
		aliasVO.setName("jhon-two");
		aliasVO.setDomain(domainName);
		accountVO.getAliases().add(aliasVO);
		template.put(url, accountVO);
		boolean hasTheAlias=false;
		for (AccountAliasVO aliasOnAccountVO : accountService.read(domainName,accountName).getAliases()){
			if(aliasOnAccountVO.getName().equals("jhon-two")){
				hasTheAlias=true;
				break;
			}
		}
		Assert.assertTrue(hasTheAlias);
		accountService.delete(domainName,accountName);
	}
	
	@Test
	public void delete(){
		String accountName = "jhon";
		String url = (base+"/"+accountName+type).replace("{domain}", domainName);
		try{accountService.delete(domainName,accountName);}catch(UnknownResourceException e){}
		AccountVO accountVO = new AccountVO();
		accountVO.setDomain(domainName);
		accountVO.setAccount(accountName);
		try{accountService.delete(domainName,accountName);}catch(UnknownResourceException e){}
		accountService.create(accountVO);
		template.delete(url);
	}

	@Test
	public void readAllAccountProperties(){
		String accountName = "jhon";
		String url = (base+"/{account}/properties"+type).replace("{domain}", domainName).replace("{account}", accountName);
		try{accountService.delete(domainName,accountName);}catch(UnknownResourceException e){}
		AccountVO accountVO = new AccountVO();
		accountVO.setDomain(domainName);
		accountVO.setAccount(accountName);
		accountService.create(accountVO);
		template.getForObject(url, Object.class);
		accountService.delete(domainName,accountName);
	}

	@Test
	public void updateAllAccountProperties(){
		String accountName = "jhon";
		String url = (base+"/{account}/properties"+type).replace("{domain}", domainName).replace("{account}", accountName);
		try{accountService.delete(domainName,accountName);}catch(UnknownResourceException e){}
		AccountVO accountVO = new AccountVO();
		accountVO.setDomain(domainName);
		accountVO.setAccount(accountName);
		accountService.create(accountVO);
		AccountPropertiesVO properties = new AccountPropertiesVO();
		AccountPropertyVO property = new AccountPropertyVO();
		property.setName("keysomething");
		property.setValue("somethingvalue");
		properties.add(property);
		template.put(url, properties);
		Assert.assertTrue(accountService.readProperties(domainName,accountName).size()>0);
		accountService.delete(domainName,accountName);
	}

	@Test
	public void deleteAllAccountProperties(){
		String accountName = "jhon";
		String url = (base+"/{account}/properties"+type).replace("{domain}", domainName).replace("{account}", accountName);
		try{accountService.delete(domainName,accountName);}catch(UnknownResourceException e){}
		AccountVO accountVO = new AccountVO();
		accountVO.setDomain(domainName);
		accountVO.setAccount(accountName);
		accountService.create(accountVO);
		template.delete(url);
		accountService.delete(domainName,accountName);
	}
}
