package org.mxhero.webapi.restful.integration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.webapi.Utils;
import org.mxhero.webapi.service.SystemPropertiesService;
import org.mxhero.webapi.service.UserService;
import org.mxhero.webapi.service.exception.UnknownResourceException;
import org.mxhero.webapi.vo.SystemPropertyVO;
import org.mxhero.webapi.vo.UserVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;


@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = {"file:src/test/resources/infrastructure-context.xml","file:src/test/resources/admin-test-context.xml"})
public class SystemPropertyControllerTest {

	@Autowired
	private UserService userService;
	@Autowired
	private SystemPropertiesService propertiesService;
	@Autowired
	private RestTemplate template;
	private String base = "http://localhost:8080/webapi/api/v1/system/properties";
	private String type = ".json";
	
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
		String url = base+type;
		SystemPropertyVO property = new SystemPropertyVO();
		property.setKey("somekey");
		property.setValue("somevalue");
		try{propertiesService.delete(property.getKey());}catch(UnknownResourceException e){}
		template.postForEntity(url, property, Object.class);
		Assert.assertTrue(propertiesService.read(property.getKey()).getValue().equalsIgnoreCase(property.getValue()));
		propertiesService.delete(property.getKey());
	}

	@Test
	public void read(){
		String url = base+"/somekey"+type;
		try{propertiesService.delete("somekey");}catch(UnknownResourceException e){}
		propertiesService.create("somekey", "somevalue");
		template.getForEntity(url, Object.class);
		propertiesService.delete("somekey");
	}

	@Test
	public void update(){
		String url = base+"/somekey"+type;
		try{propertiesService.delete("somekey");}catch(UnknownResourceException e){}
		propertiesService.create("somekey", "somevalue");
		SystemPropertyVO property = new SystemPropertyVO();
		property.setKey("somekey");
		property.setValue("TEST");
		template.put(url, property);
		propertiesService.delete("somekey");
	}

	@Test
	public void delete(){
		String url = base+"/somekey"+type;
		try{propertiesService.delete("somekey");}catch(UnknownResourceException e){}
		propertiesService.create("somekey", "somevalue");
		template.delete(url);
	}
}
