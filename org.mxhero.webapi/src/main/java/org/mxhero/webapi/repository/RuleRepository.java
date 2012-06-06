package org.mxhero.webapi.repository;

import java.util.List;

import org.mxhero.webapi.vo.RuleVO;


public interface RuleRepository {

	void delete(Integer ruleId);
	
	void deleteByDomain(String domainId);
	
	void deleteByAccount(String domainId, String account);
	
	void deleteByGroup(String domainId, String group);
	
	boolean checkFromTo(String domainId, Integer featureId, String fromFreeValue, String toFreeValue, Integer ruleId);
	
	Integer insert(RuleVO rule);
	
	void update(RuleVO rule);
	
	void toggleStatus(Integer ruleId);
	
	List<RuleVO> findByDomainId(String domainId, Integer featureId);
	
	List<RuleVO> findWitNullDomain(Integer featureId);
	
	RuleVO findById(Integer ruleId);
}
