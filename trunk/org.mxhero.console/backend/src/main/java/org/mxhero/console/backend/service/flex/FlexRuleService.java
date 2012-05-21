package org.mxhero.console.backend.service.flex;

import java.util.Collection;

import org.mxhero.console.backend.service.RuleService;
import org.mxhero.console.backend.vo.FeatureRuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service("ruleService")
@RemotingDestination(channels={"flex-amf"})
public class FlexRuleService implements RuleService{

	private RuleService service;
	
	@Autowired(required=true)
	public FlexRuleService(@Qualifier("jdbcRuleService")RuleService service) {
		this.service = service;
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public Integer createRule(FeatureRuleVO ruleVO, Integer featureId,
			String domainId) {
		return service.createRule(ruleVO, featureId, domainId);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public void editRule(FeatureRuleVO ruleVO) {
		service.editRule(ruleVO);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public void remove(Integer id) {
		service.remove(id);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public void toggleStatus(Integer id) {
		service.toggleStatus(id);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public Collection<FeatureRuleVO> getRulesByDomainId(String domainId,
			Integer featureId) {
		return service.getRulesByDomainId(domainId, featureId);
	}

	@Override
	@Secured("ROLE_ADMIN")
	public Collection<FeatureRuleVO> getRulesWithoutDomain(Integer featureId) {
		return service.getRulesWithoutDomain(featureId);
	}

}
