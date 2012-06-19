package org.mxhero.webapi.restful.integration;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.webapi.Utils;
import org.mxhero.webapi.service.DomainAccountService;
import org.mxhero.webapi.service.DomainService;
import org.mxhero.webapi.service.GroupService;
import org.mxhero.webapi.service.UserService;
import org.mxhero.webapi.service.exception.UnknownResourceException;
import org.mxhero.webapi.vo.AccountVO;
import org.mxhero.webapi.vo.DomainVO;
import org.mxhero.webapi.vo.GroupVO;
import org.mxhero.webapi.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = {"file:src/test/resources/infrastructure-context.xml","file:src/test/resources/admin-test-context.xml"})
public class DomainGroupControllerTest {

	@Autowired
	private UserService userService;
	@Autowired
	private DomainService domainService;
	@Autowired
	private DomainAccountService accountService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private RestTemplate template;
	private String base = "http://localhost:8080/webapi/api/v1/domains/{domain}/groups";
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
		String groupName = "test-group";
		try{groupService.delete(domainName,groupName);}catch(UnknownResourceException e){}
		GroupVO groupVO = new GroupVO();
		groupVO.setName(groupName);
		groupVO.setDomain(domainName);
		template.postForObject(url, groupVO, Object.class);
		groupService.delete(domainName,groupName);
	}

	@Test
	public void read(){
		String url = (base+"/{name}"+type).replace("{domain}", domainName);
		String groupName = "test-group";
		try{groupService.delete(domainName,groupName);}catch(UnknownResourceException e){}
		GroupVO groupVO = new GroupVO();
		groupVO.setName(groupName);
		groupVO.setDomain(domainName);
		groupService.create(groupVO);
		template.getForObject(url.replace("{name}", groupVO.getName()), Object.class);
		groupService.delete(domainName,groupName);
	}

	@Test
	public void update(){
		String url = (base+"/{name}"+type).replace("{domain}", domainName);
		String groupName = "test-group";
		try{groupService.delete(domainName,groupName);}catch(UnknownResourceException e){}
		GroupVO groupVO = new GroupVO();
		groupVO.setName(groupName);
		groupVO.setDomain(domainName);
		groupService.create(groupVO);
		groupVO = groupService.read(domainName,groupName);
		groupVO.setDescription("TEST");
		template.put(url.replace("{name}", groupVO.getName()), groupVO);
		Assert.assertTrue(groupService.read(domainName,groupName).getDescription().equals(groupVO.getDescription()));
		groupService.delete(domainName,groupName);
	}

	@Test
	public void delete(){
		String url = (base+"/{name}"+type).replace("{domain}", domainName);
		String groupName = "test-group";
		try{groupService.delete(domainName,groupName);}catch(UnknownResourceException e){}
		GroupVO groupVO = new GroupVO();
		groupVO.setName(groupName);
		groupVO.setDomain(domainName);
		groupService.create(groupVO);
		template.delete(url.replace("{name}", groupName));
	}
	
	@Test
	public void readAllNoGroupAccounts(){
		String url = (base+"/accounts/available"+type).replace("{domain}", domainName);
		String accountName = "jhon";
		String groupName = "test-group";
		try{groupService.delete(domainName,groupName);}catch(UnknownResourceException e){}
		GroupVO groupVO = new GroupVO();
		groupVO.setName(groupName);
		groupVO.setDomain(domainName);
		groupService.create(groupVO);
		try{accountService.delete(domainName,accountName);}catch(UnknownResourceException e){}
		AccountVO accountVO = new AccountVO();
		accountVO.setDomain(domainName);
		accountVO.setAccount(accountName);
		accountService.create(accountVO);
		template.getForObject(url, Object.class);
		accountService.delete(domainName,accountName);
		groupService.delete(domainName,groupName);
	}

	@Test
	public void readAllGroupAccounts(){
		String url = (base+"/{name}/accounts"+type).replace("{domain}", domainName);
		String accountName = "jhon";
		String groupName = "test-group";
		try{groupService.delete(domainName,groupName);}catch(UnknownResourceException e){}
		GroupVO groupVO = new GroupVO();
		groupVO.setName(groupName);
		groupVO.setDomain(domainName);
		groupService.create(groupVO);
		try{accountService.delete(domainName,accountName);}catch(UnknownResourceException e){}
		AccountVO accountVO = new AccountVO();
		accountVO.setDomain(domainName);
		accountVO.setAccount(accountName);
		accountVO.setGroup(groupName);
		accountService.create(accountVO);
		template.getForObject(url.replace("{name}", groupVO.getName()), Object.class);
		accountService.delete(domainName,accountName);
		groupService.delete(domainName,groupName);
	}

	@Test
	public void addAccount(){
		String url = (base+"/{name}/accounts/{account}/add"+type).replace("{domain}", domainName);
		String accountName = "jhon";
		String groupName = "test-group";
		try{groupService.delete(domainName,groupName);}catch(UnknownResourceException e){}
		GroupVO groupVO = new GroupVO();
		groupVO.setName(groupName);
		groupVO.setDomain(domainName);
		groupService.create(groupVO);
		try{accountService.delete(domainName,accountName);}catch(UnknownResourceException e){}
		AccountVO accountVO = new AccountVO();
		accountVO.setDomain(domainName);
		accountVO.setAccount(accountName);
		accountService.create(accountVO);
		template.postForObject(url.replace("{name}", groupName).replace("{account}", accountName),null,Object.class);
		Assert.assertTrue(accountService.read(domainName,accountName).getGroup().equals(groupName));
		accountService.delete(domainName,accountName);
		groupService.delete(domainName,groupName);
	}

	@Test
	public void removeAccount(){
		String url = (base+"/{name}/accounts/{account}/remove"+type).replace("{domain}", domainName);
		String accountName = "jhon";
		String groupName = "test-group";
		try{groupService.delete(domainName,groupName);}catch(UnknownResourceException e){}
		GroupVO groupVO = new GroupVO();
		groupVO.setName(groupName);
		groupVO.setDomain(domainName);
		groupService.create(groupVO);
		try{accountService.delete(domainName,accountName);}catch(UnknownResourceException e){}
		AccountVO accountVO = new AccountVO();
		accountVO.setDomain(domainName);
		accountVO.setAccount(accountName);
		accountService.create(accountVO);
		template.delete(url.replace("{name}", groupName).replace("{account}", accountName));
		Assert.assertNull(accountService.read(domainName,accountName).getGroup());
		accountService.delete(domainName,accountName);
		groupService.delete(domainName,groupName);
	}
	
}
