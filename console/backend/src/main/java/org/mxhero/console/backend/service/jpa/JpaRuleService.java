package org.mxhero.console.backend.service.jpa;

import java.util.Calendar;

import org.mxhero.console.backend.dao.DomainDao;
import org.mxhero.console.backend.dao.FeatureDao;
import org.mxhero.console.backend.dao.FeatureRuleDao;
import org.mxhero.console.backend.entity.Domain;
import org.mxhero.console.backend.entity.Feature;
import org.mxhero.console.backend.entity.FeatureRule;
import org.mxhero.console.backend.entity.FeatureRuleDirection;
import org.mxhero.console.backend.infrastructure.BusinessException;
import org.mxhero.console.backend.service.RuleService;
import org.mxhero.console.backend.vo.FeatureRuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;

@Service("ruleService")
@RemotingDestination(channels={"flex-amf"})
public class JpaRuleService implements RuleService{

	private static final String FEATURE_NOT_FOUND="feature.not.found";
	
	private static final String RULE_NOT_FOUND="rule.not.found";
	
	private FeatureRuleDao dao;
	
	private FeatureDao featureDao;
	
	private DomainDao domainDao;
	
	@Autowired
	public JpaRuleService(FeatureRuleDao dao, FeatureDao featureDao,
			DomainDao domainDao) {
		super();
		this.dao = dao;
		this.featureDao = featureDao;
		this.domainDao = domainDao;
	}

	@Override
	public void createRule(FeatureRuleVO ruleVO, Integer featureId) {
		FeatureRule rule = new FeatureRule();
		
		Feature feature = featureDao.readByPrimaryKey(featureId);
		if(feature==null){
			throw new BusinessException(FEATURE_NOT_FOUND);
		}
		
		rule.setCreated(Calendar.getInstance());
		rule.setDomain(null);
		rule.setEnabled(ruleVO.getEnabled());
		rule.setFeature(feature);
		rule.setLabel(ruleVO.getName());
		rule.setUpdated(Calendar.getInstance());
		
		FeatureRuleDirection fromDirection = new FeatureRuleDirection();
		fromDirection.setDirectionType(ruleVO.getFromDirection().getDirectionType());
		fromDirection.setFreeValue(ruleVO.getFromDirection().getFreeValue());
		if(ruleVO.getFromDirection().getValueId()!=null &&
				ruleVO.getFromDirection().getValueId()>-1){
			fromDirection.setValueId(ruleVO.getFromDirection().getValueId());
		}
		fromDirection.setRule(rule);
	
		FeatureRuleDirection toDirection = new FeatureRuleDirection();
		toDirection.setDirectionType(ruleVO.getToDirection().getDirectionType());
		toDirection.setFreeValue(ruleVO.getToDirection().getFreeValue());
		if(ruleVO.getToDirection().getValueId()!=null &&
				ruleVO.getToDirection().getValueId()>-1){
			toDirection.setValueId(ruleVO.getToDirection().getValueId());
		}
		toDirection.setRule(rule);
		
		rule.setFromDirection(fromDirection);
		rule.setToDirection(toDirection);
		
		feature.getRules().add(rule);
		featureDao.save(feature);
	}

	@Override
	public void createRule(FeatureRuleVO ruleVO, Integer featureId,
			Integer domainId) {
		FeatureRule rule = new FeatureRule();
		
		Domain domain = domainDao.readByPrimaryKey(domainId);
		if(domain==null){
			throw new BusinessException(JpaDomainService.DOMAIN_NOT_EXISTS);
		}
		Feature feature = featureDao.readByPrimaryKey(featureId);
		
		if(feature==null){
			throw new BusinessException(FEATURE_NOT_FOUND);
		}
		
		rule.setCreated(Calendar.getInstance());
		rule.setDomain(domain);
		rule.setEnabled(ruleVO.getEnabled());
		rule.setFeature(feature);
		rule.setLabel(ruleVO.getName());
		rule.setUpdated(Calendar.getInstance());
		feature.getRules().add(rule);
		featureDao.save(feature);
	}

	@Override
	public void editRule(FeatureRuleVO ruleVO) {
		FeatureRule rule = dao.readByPrimaryKey(ruleVO.getId());
		if(rule == null){
			throw new BusinessException(RULE_NOT_FOUND);
		}
		rule.setUpdated(Calendar.getInstance());
		rule.setLabel(ruleVO.getName());
		
		rule.getFromDirection().setFreeValue(ruleVO.getFromDirection().getFreeValue());
		rule.getFromDirection().setDirectionType(ruleVO.getFromDirection().getDirectionType());
		if(ruleVO.getFromDirection().getValueId()!=null &&
				ruleVO.getFromDirection().getValueId()>-1){
			rule.getFromDirection().setValueId(ruleVO.getFromDirection().getValueId());
		}else {
			rule.getFromDirection().setValueId(null);
		}

		rule.getToDirection().setFreeValue(ruleVO.getToDirection().getFreeValue());
		rule.getToDirection().setDirectionType(ruleVO.getToDirection().getDirectionType());
		if(ruleVO.getToDirection().getValueId()!=null &&
				ruleVO.getToDirection().getValueId()>-1){
			rule.getToDirection().setValueId(ruleVO.getToDirection().getValueId());
		}else {
			rule.getToDirection().setValueId(null);
		}
		
		
		dao.save(rule);
	}

}
