package org.mxhero.webapi.restful.integration;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.webapi.Utils;
import org.mxhero.webapi.service.DomainAccountService;
import org.mxhero.webapi.service.DomainService;
import org.mxhero.webapi.service.UserService;
import org.mxhero.webapi.service.exception.UnknownResourceException;
import org.mxhero.webapi.vo.AccountVO;
import org.mxhero.webapi.vo.DomainVO;
import org.mxhero.webapi.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = {"file:src/test/resources/infrastructure-context.xml","file:src/test/resources/admin-test-context.xml"})
public class UserDomainControllerTest {

	@Autowired
	private UserService userService;
	@Autowired
	private DomainService domainService;
	@Autowired
	private RestTemplate template;
	@Autowired
	private DomainAccountService accountService;
	private String base = "http://localhost:8080/webapi/api/v1/domains/{domain}/users";
	private String domainName = "test.com";
	private String domainUser = "smith";
	@Before
	public void init(){
		UserVO user =  Utils.getUser(domainUser);
		user.setDomain(domainName);
		user.setNotifyEmail("test@test.com");
		try{domainService.delete(domainName);}catch(UnknownResourceException e){}
		DomainVO domain = new DomainVO();
		domain.setDomain(domainName);
		domain.setCreationDate(Calendar.getInstance());
		domain.setServer("smtp.test.com");
		domain.setUpdatedDate(Calendar.getInstance());
		domainService.create(domain);
		try{userService.delete(domainUser, domainName);}catch(UnknownResourceException e){}
		userService.create(user, UserVO.ROLE_DOMAIN_ADMIN);
	}
	
	@After
	public void clear(){
		userService.delete("smith", "test.com");
		domainService.delete("test.com");
	}
	
	@Test
	public void readAll() {	
		String url = (base+".json").replace("{domain}", domainName);
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("limit", "10");
		parameters.put("offset", "0");
		template.getForEntity(url, Object.class, parameters); 
		Assert.assertTrue(true);
	}

	@Test
	public void create(){
		String url = (base+".json").replace("{domain}", domainName);
		UserVO user = Utils.getUser("accounttest");
		user.setAccount("accounttest");
		user.setDomain(domainName);
		try{userService.delete("accounttest", domainName);}catch(UnknownResourceException e){}
		AccountVO accountVO = new AccountVO();
		accountVO.setAccount("accounttest");
		accountVO.setDomain(domainName);
		accountService.create(accountVO);
		try{userService.delete("accounttest", domainName);}catch(UnknownResourceException e){}
		template.postForEntity(url, user, Object.class);
		userService.delete("accounttest", domainName);
		accountService.delete(domainName, "accounttest");
	}

	@Test
	public void read() {
		String accountName="accounttest";
		String url = (base+"/"+accountName+".json").replace("{domain}", domainName);
		UserVO user = Utils.getUser(accountName);
		user.setAccount(accountName);
		user.setDomain(domainName);
		try{userService.delete(accountName, domainName);}catch(UnknownResourceException e){}
		AccountVO accountVO = new AccountVO();
		accountVO.setAccount(accountName);
		accountVO.setDomain(domainName);
		accountService.create(accountVO);
		try{userService.delete(accountName, domainName);}catch(UnknownResourceException e){}
		userService.create(user, UserVO.ROLE_DOMAIN_ACCOUNT);
		template.getForEntity(url, Object.class);
		userService.delete(accountName, domainName);
		accountService.delete(domainName, accountName);
	}

	@Test
	public void update(){
		String accountName="accounttest";
		String url = (base+"/"+accountName+".json").replace("{domain}", domainName);
		UserVO user = Utils.getUser(accountName);
		user.setAccount(accountName);
		user.setDomain(domainName);
		try{userService.delete(accountName, domainName);}catch(UnknownResourceException e){}
		AccountVO accountVO = new AccountVO();
		accountVO.setAccount(accountName);
		accountVO.setDomain(domainName);
		accountService.create(accountVO);
		try{userService.delete(accountName, domainName);}catch(UnknownResourceException e){}
		userService.create(user, UserVO.ROLE_DOMAIN_ACCOUNT);
		user.setLastName("TEST");
		template.put(url, user);
		Assert.assertTrue(userService.read(accountName).getLastName().equals(user.getLastName()));
		userService.delete(accountName, domainName);
		accountService.delete(domainName, accountName);
	}

	@Test
	public void delete(){
		String accountName="accounttest";
		String url = (base+"/"+accountName+".json").replace("{domain}", domainName);
		UserVO user = Utils.getUser(accountName);
		user.setAccount(accountName);
		user.setDomain(domainName);
		try{userService.delete(accountName, domainName);}catch(UnknownResourceException e){}
		AccountVO accountVO = new AccountVO();
		accountVO.setAccount(accountName);
		accountVO.setDomain(domainName);
		accountService.create(accountVO);
		try{userService.delete(accountName, domainName);}catch(UnknownResourceException e){}
		userService.create(user, UserVO.ROLE_DOMAIN_ACCOUNT);
		template.delete(url);
		accountService.delete(domainName, accountName);
	}

	@Test
	public void changePassword(){
		String accountName="accounttest";
		String url = (base+"/"+accountName+"/setPassword.json?newPassword={newPassword}&oldPassword={oldPassword}").replace("{domain}", domainName);
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("newPassword", "test@test.com");
		parameters.put("oldPassword", "password");
		UserVO user = Utils.getUser(accountName);
		user.setAccount(accountName);
		user.setDomain(domainName);
		try{userService.delete(accountName, domainName);}catch(UnknownResourceException e){}
		AccountVO accountVO = new AccountVO();
		accountVO.setAccount(accountName);
		accountVO.setDomain(domainName);
		accountService.create(accountVO);
		try{userService.delete(accountName, domainName);}catch(UnknownResourceException e){}
		userService.create(user, UserVO.ROLE_DOMAIN_ACCOUNT);
		template.put(url,  null, parameters);
		userService.delete(accountName, domainName);
		accountService.delete(domainName, accountName);
	}

	@Test
	public void setPassword(){
		String accountName="accounttest";
		String url = (base+"/"+accountName+"/setPassword.json?newPassword={newPassword}").replace("{domain}", domainName);
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("newPassword", "test@test.com");
		UserVO user = Utils.getUser(accountName);
		user.setAccount(accountName);
		user.setDomain(domainName);
		try{userService.delete(accountName, domainName);}catch(UnknownResourceException e){}
		AccountVO accountVO = new AccountVO();
		accountVO.setAccount(accountName);
		accountVO.setDomain(domainName);
		accountService.create(accountVO);
		try{userService.delete(accountName, domainName);}catch(UnknownResourceException e){}
		userService.create(user, UserVO.ROLE_DOMAIN_ACCOUNT);
		template.put(url, null, parameters);
		userService.delete(accountName, domainName);
		accountService.delete(domainName, accountName);
	}
}
