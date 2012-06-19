package org.mxhero.webapi.restful.integration;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.webapi.Utils;
import org.mxhero.webapi.service.RuleService;
import org.mxhero.webapi.service.UserService;
import org.mxhero.webapi.vo.RuleDirectionVO;
import org.mxhero.webapi.vo.RulePropertyVO;
import org.mxhero.webapi.vo.RuleVO;
import org.mxhero.webapi.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = {"file:src/test/resources/infrastructure-context.xml","file:src/test/resources/admin-test-context.xml"})
public class RuleControllerTest {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RuleService ruleService;

	@Autowired
	private RestTemplate template;
	private String base = "http://localhost:8080/webapi/api/v1/rules";
	private String type = ".json";
	
	@Before
	public void init(){
		Utils.setUser(Utils.getUser("admin"), UserVO.ROLE_ADMIN, userService);
	}
	
	@Test
	public void readAll(){
		String url = (base+type);
		template.getForObject(url, Object.class);
	}
	
	@Test
	public void create(){
		String url = (base+type);
		RuleVO ruleVO = getRule();
		ruleVO = template.postForObject(url, ruleVO, RuleVO.class);
		ruleService.delete(ruleVO.getId());
	}

	@Test
	public void read(){
		String url = (base+"/{id}"+type);
		RuleVO ruleVO = getRule();
		ruleVO = ruleService.create(ruleVO);
		RuleVO readRuleVO = template.getForObject(url.replace("{id}", ruleVO.getId().toString()), RuleVO.class);
		ruleService.delete(ruleVO.getId());
		Assert.assertTrue(ruleVO.getId().equals(readRuleVO.getId()));
	}
	
	@Test
	public void update(){
		String url = (base+"/{id}"+type);
		RuleVO ruleVO = getRule();
		ruleVO = ruleService.create(ruleVO);
		ruleVO.setEnabled(false);
		template.put(url.replace("{id}", ruleVO.getId().toString()), ruleVO);
		ruleVO = ruleService.read(ruleVO.getId());
		ruleService.delete(ruleVO.getId());
		Assert.assertTrue(!ruleVO.getEnabled());
	}

	@Test
	public void status(){

	}

	@Test
	public void delete(){
		
	}
	
	private RuleVO getRule(){
		RuleVO ruleVO = new RuleVO();
		ruleVO.setComponent("org.mxhero.feature.attachmentblock");
		ruleVO.setTwoWays(false);
		ruleVO.setName("some name");
		RuleDirectionVO from = new RuleDirectionVO();
		from.setDirectionType("anyone");
		from.setFreeValue("anyone");
		RuleDirectionVO to = new RuleDirectionVO();
		to.setDirectionType("domain");
		to.setFreeValue("test.com");
		to.setDomain("test.com");
		ruleVO.setFromDirection(from);
		ruleVO.setToDirection(to);
		ruleVO.setProperties(new ArrayList<RulePropertyVO>());
		RulePropertyVO property = new RulePropertyVO();
		property.setPropertyKey("somekey");
		property.setPropertyValue("somevalue");
		ruleVO.getProperties().add(property);
		return ruleVO;
	}
}
