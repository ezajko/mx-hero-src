package org.mxhero.webapi.repository;

import java.util.List;

import org.mxhero.webapi.vo.RuleVO;


public interface RuleRepository {

	void delete(Long ruleId);
	
	void deleteByDomain(String domain);
	
	void deleteByAccount(String domain, String account);
	
	void deleteByGroup(String domain, String group);
	
	boolean checkFromTo(String domain, String component, String fromFreeValue, String toFreeValue);
	
	Long insert(RuleVO rule);
	
	void update(RuleVO rule);
	
	void toggleStatus(Long ruleId);
	
	List<RuleVO> findByDomainAndAccount(String domain, String account, String component);
	
	List<RuleVO> findWitNullDomain(String component);
	
	RuleVO findById(Long ruleId);
	
}
