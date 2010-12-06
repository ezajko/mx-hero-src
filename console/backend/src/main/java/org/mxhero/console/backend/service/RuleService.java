package org.mxhero.console.backend.service;

import org.mxhero.console.backend.vo.FeatureRuleVO;
import org.springframework.security.access.annotation.Secured;

public interface RuleService {

	@Secured("ROLE_ADMIN")
	void createRule(FeatureRuleVO ruleVO, Integer featureId);

	@Secured("ROLE_DOMAIN_ADMIN")
	void createRule(FeatureRuleVO ruleVO, Integer featureId, Integer domainId);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	void editRule(FeatureRuleVO ruleVO);
}
