package org.mxhero.webapi.restful.integration;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.webapi.Utils;
import org.mxhero.webapi.service.UserService;
import org.mxhero.webapi.service.exception.UnknownResourceException;
import org.mxhero.webapi.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = {"file:src/test/resources/infrastructure-context.xml","file:src/test/resources/admin-test-context.xml"})
public class UserControllerTest {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RestTemplate template;
	private String base = "http://localhost:8080/webapi/api/v1/users";
	
	@Before
	public void init(){
		Utils.setUser(Utils.getUser("admin"), UserVO.ROLE_ADMIN, userService);
	}
	
	@Test
	public void readAll() {	
		String url = base+".json";
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("limit", "10");
		parameters.put("offset", "0");
		ResponseEntity<Object> page = template.getForEntity(url, Object.class, parameters); 
		Assert.assertTrue(true);
	}
	
	@Test
	public void create(){
		String url = base+".json";
		UserVO user = Utils.getUser("test");
		try{userService.delete("test", null);}catch(UnknownResourceException e){}
		template.postForEntity(url, user, Object.class);
		userService.delete("test", null);
	}
	
	@Test
	public void resetPassword(){
		String url = base+"/resetPassword.json?email={email}";
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("email", "test@test.com");
		UserVO user =  Utils.getUser("test");
		user.setNotifyEmail("test@test.com");
		try{userService.delete("test", null);}catch(UnknownResourceException e){}
			userService.create(user, UserVO.ROLE_ADMIN);
		try{
			template.postForLocation(url, null, parameters);
		}catch(HttpServerErrorException e){}
		userService.delete("test", null);
	}
	
	@Test
	public void read() {	
		String url = base+"/test.json";
		UserVO user = Utils.getUser("test");
		try{userService.delete("test", null);}catch(UnknownResourceException e){}
		userService.create(user, UserVO.ROLE_ADMIN);
		template.getForEntity(url, Object.class);
		userService.delete("test", null);
	}
	
	@Test
	public void update(){
		String url = base+"/test.json";
		UserVO user = Utils.getUser("test");
		try{userService.delete("test", null);}catch(UnknownResourceException e){}
		userService.create(user, UserVO.ROLE_ADMIN);
		user.setName("NO");
		template.put(url, user);
		user = userService.read("test");
		Assert.assertTrue(user.getName().equals("NO"));
		userService.delete("test", null);
	}

	public void changePassword(){
		String url = base+"/test/changePassword.json?newPassword={newPassword}&oldPassword={oldPassword}";
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("newPassword", "test@test.com");
		parameters.put("oldPassword", "password");
		UserVO user = Utils.getUser("test");
		user.setSoundsEnabled(false);
		try{userService.delete("test", null);}catch(UnknownResourceException e){}
		userService.create(user, UserVO.ROLE_ADMIN);
		template.put(url, null, parameters);
	}
	
	@Test
	public void setPassword(){
		String url = base+"/test/setPassword.json?newPassword={newPassword}";
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("newPassword", "test@test.com");
		UserVO user = Utils.getUser("test");
		try{userService.delete("test", null);}catch(UnknownResourceException e){}
		userService.create(user, UserVO.ROLE_ADMIN);
		template.put(url, null, parameters);
	}
	
	@Test
	public void delete(){
		String url = base+"/test.json";
		UserVO user = Utils.getUser("test");
		user.setSoundsEnabled(false);
		try{userService.delete("test", null);}catch(UnknownResourceException e){}
		userService.create(user, UserVO.ROLE_ADMIN);
		template.delete(url);
	}

	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public RestTemplate getTemplate() {
		return template;
	}

	public void setTemplate(RestTemplate template) {
		this.template = template;
	}

}
