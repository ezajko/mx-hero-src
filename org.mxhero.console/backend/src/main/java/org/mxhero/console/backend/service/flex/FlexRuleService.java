/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
