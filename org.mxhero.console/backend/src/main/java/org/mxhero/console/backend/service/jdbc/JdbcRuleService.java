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

package org.mxhero.console.backend.service.jdbc;

import java.util.Collection;

import org.mxhero.console.backend.infrastructure.BusinessException;
import org.mxhero.console.backend.repository.DomainRepository;
import org.mxhero.console.backend.repository.FeatureRepository;
import org.mxhero.console.backend.repository.FeatureRuleRepository;
import org.mxhero.console.backend.service.RuleService;
import org.mxhero.console.backend.vo.FeatureRuleVO;
import org.mxhero.console.backend.vo.FeatureVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("jdbcRuleService")
public class JdbcRuleService implements RuleService{

	private static final String FEATURE_NOT_FOUND="feature.not.found";
	private static final String RULE_NOT_FOUND="rule.not.found";
	private static final String RULE_DIRECTION_EXISTS="rule.direction.exists";

	private FeatureRuleRepository ruleRepository;
	private DomainRepository domainRepository;
	private FeatureRepository featureRepository;
	
	@Autowired
	public JdbcRuleService(FeatureRuleRepository ruleRepository,
			DomainRepository domainRepository,
			FeatureRepository featureRepository) {
		this.ruleRepository = ruleRepository;
		this.domainRepository = domainRepository;
		this.featureRepository = featureRepository;
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public Integer createRule(FeatureRuleVO ruleVO, Integer featureId,
			String domainId) {

		if(domainId!=null){
			if(domainRepository.findById(domainId)==null){
				throw new BusinessException(JdbcDomainService.DOMAIN_NOT_EXISTS);
			}
		}
		FeatureVO feature =  featureRepository.findById(featureId);
		if(feature==null){
			throw new BusinessException(FEATURE_NOT_FOUND);
		}
		if(ruleRepository.checkFromTo(domainId, featureId, ruleVO.getFromDirection().getFreeValue(), ruleVO.getToDirection().getFreeValue(),null)){
			throw new BusinessException(RULE_DIRECTION_EXISTS);
		}
		ruleVO.setFeatureId(featureId);
		ruleVO.setDomain(domainId);
		return ruleRepository.insert(ruleVO);
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void editRule(FeatureRuleVO ruleVO) {
		FeatureRuleVO rule = ruleRepository.findById(ruleVO.getId());
		if(rule == null){
			throw new BusinessException(RULE_NOT_FOUND);
		}
		if(ruleRepository.checkFromTo(rule.getDomain(), rule.getFeatureId(), ruleVO.getFromDirection().getFreeValue(), ruleVO.getToDirection().getFreeValue(),rule.getId())){
			throw new BusinessException(RULE_DIRECTION_EXISTS);
		}
		ruleRepository.update(ruleVO);
	}

	@Override
	public void remove(Integer id) {
		ruleRepository.delete(id);
	}

	@Override
	public void toggleStatus(Integer id) {
		ruleRepository.toggleStatus(id);
	}

	@Override
	public Collection<FeatureRuleVO> getRulesByDomainId(String domainId,
			Integer featureId) {
		return ruleRepository.findByDomainId(domainId, featureId);
	}

	@Override
	public Collection<FeatureRuleVO> getRulesWithoutDomain(Integer featureId) {
		return ruleRepository.findWitNullDomain(featureId);
	}

}
