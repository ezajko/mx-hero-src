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
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("ruleService")
@RemotingDestination(channels={"flex-amf"})
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
	public void createRule(FeatureRuleVO ruleVO, Integer featureId,
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
		ruleRepository.insert(ruleVO);
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
