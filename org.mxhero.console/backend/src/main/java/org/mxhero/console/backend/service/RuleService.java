package org.mxhero.console.backend.service;

import java.util.Collection;

import org.mxhero.console.backend.vo.FeatureRuleVO;
import org.springframework.security.access.annotation.Secured;

public interface RuleService {

	@Secured("ROLE_DOMAIN_ADMIN")
	void createRule(FeatureRuleVO ruleVO, Integer featureId, String domainId);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	void editRule(FeatureRuleVO ruleVO);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	void remove(Integer id);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	void toggleStatus(Integer id);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	Collection<FeatureRuleVO> getRulesByDomainId(String domainId, Integer featureId);
	
	@Secured("ROLE_ADMIN")
	Collection<FeatureRuleVO> getRulesWithoutDomain(Integer featureId);
}
