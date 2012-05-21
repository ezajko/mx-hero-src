package org.mxhero.console.backend.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.mxhero.console.backend.service.RuleService;
import org.mxhero.console.backend.vo.FeatureRuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@WebService
public class RuleEndPoint {

	private RuleService ruleService;
	
	@Autowired
	public RuleEndPoint(@Qualifier("jdbcRuleService")RuleService ruleService) {
		this.ruleService = ruleService;
	}

	@WebMethod(action="createRule")
	public Integer createRule(
			@WebParam(name="ruleVO")FeatureRuleVO ruleVO, 
			@WebParam(name="featureId")Integer featureId, 
			@WebParam(name="domainId")String domainId){
		return ruleService.createRule(ruleVO, featureId, domainId);
	}
	
	@WebMethod(action="removeRule")
	public void removeRule(
			@WebParam(name="ruleId")Integer ruleId){
		ruleService.remove(ruleId);
	}
	
	@WebMethod(action="toggleStatus")
	public void toggleStatus(
			@WebParam(name="ruleId")Integer ruleId){
		ruleService.toggleStatus(ruleId);
	}
	
}
