package org.mxhero.console.backend.service;

import java.util.Collection;

import org.mxhero.console.backend.vo.FeatureRuleVO;

public interface RuleService {

	void createRule(FeatureRuleVO ruleVO, Integer featureId, String domainId);

	void editRule(FeatureRuleVO ruleVO);

	void remove(Integer id);

	void toggleStatus(Integer id);

	Collection<FeatureRuleVO> getRulesByDomainId(String domainId, Integer featureId);

	Collection<FeatureRuleVO> getRulesWithoutDomain(Integer featureId);
}
