package org.mxhero.webapi.restful.integration;

import java.util.Calendar;

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
public class UserDomainAccountControllerTest {

	@Autowired
	private UserService userService;
	@Autowired
	private DomainService domainService;
	@Autowired
	private DomainAccountService accountService;
	@Autowired
	private RestTemplate template;
	private String base = "http://localhost:8080/webapi/api/v1/domains/{domain}/accounts/{account}/user.json";
	
	@Before
	public void init(){
		Utils.setUser(Utils.getUser("admin"), UserVO.ROLE_ADMIN, userService);
	}
	
	@Test
	public void read(){
		
		String url = base.replace("{domain}", "test.com").replace("{account}", "smith");
		UserVO user =  Utils.getUser("smith");
		user.setDomain("test.com");
		user.setNotifyEmail("test@test.com");
		user.setAccount("smith");
		try{domainService.delete("test.com");}catch(UnknownResourceException e){}
		DomainVO domain = new DomainVO();
		domain.setDomain("test.com");
		domain.setCreationDate(Calendar.getInstance());
		domain.setServer("smtp.test.com");
		domain.setUpdatedDate(Calendar.getInstance());
		domainService.create(domain);
		try{userService.delete("smith", "test.com");}catch(UnknownResourceException e){}
		AccountVO accountVO = new AccountVO();
		accountVO.setAccount("smith");
		accountVO.setDomain("test.com");
		accountService.create(accountVO);
		userService.create(user, UserVO.ROLE_DOMAIN_ACCOUNT);
		template.getForEntity(url, Object.class);
		userService.delete("smith", "test.com");
		domainService.delete("test.com");
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public DomainService getDomainService() {
		return domainService;
	}

	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}

	public RestTemplate getTemplate() {
		return template;
	}

	public void setTemplate(RestTemplate template) {
		this.template = template;
	}

	public DomainAccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(DomainAccountService accountService) {
		this.accountService = accountService;
	}

}
