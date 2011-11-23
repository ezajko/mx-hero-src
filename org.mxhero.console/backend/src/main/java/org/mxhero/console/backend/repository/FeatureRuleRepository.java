package org.mxhero.console.backend.repository;

import java.util.List;

import org.mxhero.console.backend.vo.FeatureRuleVO;

public interface FeatureRuleRepository {

	void delete(Integer ruleId);
	
	void deleteByDomain(String domainId);
	
	void deleteByAccount(String domainId, String account);
	
	void deleteByGroup(String domainId, String group);
	
	boolean checkFromTo(String domainId, Integer featureId, String fromFreeValue, String toFreeValue, Integer ruleId);
	
	void insert(FeatureRuleVO rule);
	
	void update(FeatureRuleVO rule);
	
	void toggleStatus(Integer ruleId);
	
	List<FeatureRuleVO> findByDomainId(String domainId, Integer featureId);
	
	List<FeatureRuleVO> findWitNullDomain(Integer featureId);
	
	FeatureRuleVO findById(Integer ruleId);
}
