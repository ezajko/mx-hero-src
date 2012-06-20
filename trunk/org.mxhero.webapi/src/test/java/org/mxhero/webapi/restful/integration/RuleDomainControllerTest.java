package org.mxhero.webapi.restful.integration;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.webapi.Utils;
import org.mxhero.webapi.service.DomainService;
import org.mxhero.webapi.service.RuleService;
import org.mxhero.webapi.service.UserService;
import org.mxhero.webapi.service.exception.UnknownResourceException;
import org.mxhero.webapi.vo.DomainVO;
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
public class RuleDomainControllerTest {

	@Autowired
	private UserService userService;
	@Autowired
	private RuleService ruleService;
	@Autowired
	private DomainService domainService;

	@Autowired
	private RestTemplate template;
	private String base = "http://localhost:8080/webapi/api/v1/domains/{domain}/rules";
	private String type = ".json";
	private String domainName = "test.com";
	
	@Before
	public void init(){
		Utils.setUser(Utils.getUser("admin"), UserVO.ROLE_ADMIN, userService);
		List<RuleVO> rules = ruleService.readAll(null, null, null);
		if(rules!=null){
			for(RuleVO rule : rules){
				ruleService.delete(rule.getId());
			}
		}
		try{domainService.delete(domainName);}catch(UnknownResourceException e){}
		DomainVO domain = new DomainVO();
		domain.setDomain(domainName);
		domain.setServer("smtp.server");
		domainService.create(domain);
	}
	
	@Test
	public void readAll(){
		String url = (base.replace("{domain}", domainName)+"?component=org.mxhero.feature.attachmentblock"+type);
		template.getForObject(url, Object.class);
	}
	
	@Test
	public void create(){
		String url = (base.replace("{domain}", domainName)+type);
		RuleVO ruleVO = getRule();
		ruleVO = template.postForObject(url, ruleVO, RuleVO.class);
		ruleService.delete(ruleVO.getId());
	}

	@Test
	public void read(){
		String url = (base.replace("{domain}", domainName)+"/{id}"+type);
		RuleVO ruleVO = getRule();
		ruleVO = ruleService.create(ruleVO);
		RuleVO readRuleVO = template.getForObject(url.replace("{id}", ruleVO.getId().toString()), RuleVO.class);
		ruleService.delete(ruleVO.getId());
		Assert.assertTrue(ruleVO.getId().equals(readRuleVO.getId()));
	}
	
	@Test
	public void update(){
		String url = (base.replace("{domain}", domainName)+"/{id}"+type);
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
		String url = (base.replace("{domain}", domainName)+"/{id}/status"+type+"?enabled=false");
		RuleVO ruleVO = getRule();
		ruleVO = ruleService.create(ruleVO);
		template.put(url.replace("{id}", ruleVO.getId().toString()),null);
		ruleVO = ruleService.read(ruleVO.getId());
		ruleService.delete(ruleVO.getId());
		Assert.assertTrue(!ruleVO.getEnabled());
	}

	@Test
	public void delete(){
		String url = (base.replace("{domain}", domainName)+"/{id}"+type);
		RuleVO ruleVO = getRule();
		ruleVO = ruleService.create(ruleVO);
		template.delete(url.replace("{id}", ruleVO.getId().toString()));
		try{ruleService.read(ruleVO.getId());}catch(UnknownResourceException e){return;}
		Assert.fail();
	}
	
	private RuleVO getRule(){
		RuleVO ruleVO = new RuleVO();
		ruleVO.setComponent("org.mxhero.feature.attachmentblock");
		ruleVO.setTwoWays(false);
		ruleVO.setName("some name");
		ruleVO.setDomain(domainName);
		RuleDirectionVO from = new RuleDirectionVO();
		from.setDirectionType("anyone");
		from.setFreeValue("anyone");
		RuleDirectionVO to = new RuleDirectionVO();
		to.setDirectionType("domain");
		to.setFreeValue(domainName);
		to.setDomain(domainName);
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
