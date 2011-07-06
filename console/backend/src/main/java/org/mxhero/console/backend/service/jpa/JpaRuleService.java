package org.mxhero.console.backend.service.jpa;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import org.mxhero.console.backend.dao.DomainDao;
import org.mxhero.console.backend.dao.FeatureDao;
import org.mxhero.console.backend.dao.FeatureRuleDao;
import org.mxhero.console.backend.entity.Domain;
import org.mxhero.console.backend.entity.Feature;
import org.mxhero.console.backend.entity.FeatureRule;
import org.mxhero.console.backend.entity.FeatureRuleDirection;
import org.mxhero.console.backend.entity.FeatureRuleProperty;
import org.mxhero.console.backend.infrastructure.BusinessException;
import org.mxhero.console.backend.service.RuleService;
import org.mxhero.console.backend.vo.FeatureRulePropertyVO;
import org.mxhero.console.backend.vo.FeatureRuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;

@Service("ruleService")
@RemotingDestination(channels={"flex-amf"})
public class JpaRuleService implements RuleService{

	private static final String FEATURE_NOT_FOUND="feature.not.found";
	
	private static final String RULE_NOT_FOUND="rule.not.found";
	
	private static final String RULE_DIRECTION_EXISTS="rule.direction.exists";
	
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
		rule.setAdminOrder(ruleVO.getAdminOrder());
		rule.setTwoWays(ruleVO.getTwoWays());
		
		FeatureRuleDirection fromDirection = new FeatureRuleDirection();
		fromDirection.setDirectionType(ruleVO.getFromDirection().getDirectionType());
		fromDirection.setFreeValue(ruleVO.getFromDirection().getFreeValue());
		fromDirection.setDomain(ruleVO.getFromDirection().getDomain());
		fromDirection.setGroup(ruleVO.getFromDirection().getGroup());
		fromDirection.setAccount(ruleVO.getFromDirection().getAccount());
		fromDirection.setRule(rule);
	
		FeatureRuleDirection toDirection = new FeatureRuleDirection();
		toDirection.setDirectionType(ruleVO.getToDirection().getDirectionType());
		toDirection.setFreeValue(ruleVO.getToDirection().getFreeValue());
		toDirection.setDomain(ruleVO.getToDirection().getDomain());
		toDirection.setGroup(ruleVO.getToDirection().getGroup());
		toDirection.setAccount(ruleVO.getToDirection().getAccount());
		toDirection.setRule(rule);
		
		rule.setFromDirection(fromDirection);
		rule.setToDirection(toDirection);
		
		rule.setProperties(new HashSet<FeatureRuleProperty>());
		
		for(FeatureRulePropertyVO propertyVO : ruleVO.getProperties()){
			FeatureRuleProperty property = new FeatureRuleProperty();
			property.setPropertyKey(propertyVO.getPropertyKey());
			property.setPropertyValue(propertyVO.getPropertyValue());
			property.setRule(rule);
			rule.getProperties().add(property);
		}

		
		List<FeatureRule> rulesFound = dao.findCheckCreationAdmin(ruleVO.getFromDirection().getFreeValue(), 
				ruleVO.getToDirection().getFreeValue(), 
				featureId);
		if(rulesFound!=null && rulesFound.size()>0){
			throw new BusinessException(RULE_DIRECTION_EXISTS);
		}
		
		feature.getRules().add(rule);
		featureDao.save(feature);
	}

	@Override
	public void createRule(FeatureRuleVO ruleVO, Integer featureId,
			String domainId) {
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
		rule.setTwoWays(ruleVO.getTwoWays());
		
		FeatureRuleDirection fromDirection = new FeatureRuleDirection();
		fromDirection.setDirectionType(ruleVO.getFromDirection().getDirectionType());
		fromDirection.setFreeValue(ruleVO.getFromDirection().getFreeValue());
		fromDirection.setDomain(ruleVO.getFromDirection().getDomain());
		fromDirection.setGroup(ruleVO.getFromDirection().getGroup());
		fromDirection.setAccount(ruleVO.getFromDirection().getAccount());
		fromDirection.setRule(rule);
	
		FeatureRuleDirection toDirection = new FeatureRuleDirection();
		toDirection.setDirectionType(ruleVO.getToDirection().getDirectionType());
		toDirection.setFreeValue(ruleVO.getToDirection().getFreeValue());
		toDirection.setDomain(ruleVO.getToDirection().getDomain());
		toDirection.setGroup(ruleVO.getToDirection().getGroup());
		toDirection.setAccount(ruleVO.getToDirection().getAccount());
		toDirection.setRule(rule);
		
		rule.setFromDirection(fromDirection);
		rule.setToDirection(toDirection);
		
		rule.setProperties(new HashSet<FeatureRuleProperty>());
		
		for(FeatureRulePropertyVO propertyVO : ruleVO.getProperties()){
			FeatureRuleProperty property = new FeatureRuleProperty();
			property.setPropertyKey(propertyVO.getPropertyKey());
			property.setPropertyValue(propertyVO.getPropertyValue());
			property.setRule(rule);
			rule.getProperties().add(property);
		}
		List<FeatureRule> rulesFound = dao.findCheckCreation(ruleVO.getFromDirection().getFreeValue(), 
				ruleVO.getToDirection().getFreeValue(), 
				featureId, 
				domainId);
		if(rulesFound!=null && rulesFound.size()>0){
			throw new BusinessException(RULE_DIRECTION_EXISTS);
		}
		
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
		rule.setAdminOrder(ruleVO.getAdminOrder());
		rule.setTwoWays(ruleVO.getTwoWays());
		
		rule.getFromDirection().setFreeValue(ruleVO.getFromDirection().getFreeValue());
		rule.getFromDirection().setDirectionType(ruleVO.getFromDirection().getDirectionType());
		rule.getFromDirection().setDomain(ruleVO.getFromDirection().getDomain());
		rule.getFromDirection().setGroup(ruleVO.getFromDirection().getGroup());
		rule.getFromDirection().setAccount(ruleVO.getFromDirection().getAccount());

		rule.getToDirection().setFreeValue(ruleVO.getToDirection().getFreeValue());
		rule.getToDirection().setDirectionType(ruleVO.getToDirection().getDirectionType());
		rule.getToDirection().setDomain(ruleVO.getToDirection().getDomain());
		rule.getToDirection().setGroup(ruleVO.getToDirection().getGroup());
		rule.getToDirection().setAccount(ruleVO.getToDirection().getAccount());
		
		rule.setProperties(new HashSet<FeatureRuleProperty>());
		
		for(FeatureRulePropertyVO propertyVO : ruleVO.getProperties()){
			FeatureRuleProperty property = new FeatureRuleProperty();
			property.setPropertyKey(propertyVO.getPropertyKey());
			property.setPropertyValue(propertyVO.getPropertyValue());
			property.setRule(rule);
			rule.getProperties().add(property);
		}
		

		List<FeatureRule> rulesFound=null;
		if(rule.getDomain()!=null){
			rulesFound =dao.findCheckCreation(ruleVO.getFromDirection().getFreeValue(), 
					ruleVO.getToDirection().getFreeValue(), 
					rule.getFeature().getId(), 
					rule.getDomain().getDomain());
		}else{
			rulesFound =dao.findCheckCreationAdmin(ruleVO.getFromDirection().getFreeValue(), 
					ruleVO.getToDirection().getFreeValue(), 
					rule.getFeature().getId());
		}
		

		
		if(rulesFound!=null && rulesFound.size()>0 && !rulesFound.get(0).getId().equals(rule.getId())){
			throw new BusinessException(RULE_DIRECTION_EXISTS);
		}
		
		dao.save(rule);
	}

}
