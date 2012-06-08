package org.mxhero.webapi.service.jdbc;

import java.util.List;

import org.mxhero.webapi.repository.RuleRepository;
import org.mxhero.webapi.service.RuleService;
import org.mxhero.webapi.service.exception.ConflictResourceException;
import org.mxhero.webapi.service.exception.UnknownResourceException;
import org.mxhero.webapi.vo.RulePropertyVO;
import org.mxhero.webapi.vo.RuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("jdbcRuleService")
public class JdbcRuleService implements RuleService{

	private RuleRepository ruleRepository;
	
	@Autowired()
	public JdbcRuleService(RuleRepository ruleRepository) {
		this.ruleRepository = ruleRepository;
	}

	@Override
	public List<RuleVO> readAll(String domain, String component) {
		if(domain==null){
			return ruleRepository.findWitNullDomain(component);
		}
		return ruleRepository.findByDomainId(domain, component);
	}

	@Override
	public RuleVO create(RuleVO ruleVO) {
		if(ruleRepository.checkFromTo(ruleVO.getDomain(), ruleVO.getComponent(), ruleVO.getFromDirection().getFreeValue(), ruleVO.getToDirection().getFreeValue())){
			throw new ConflictResourceException("rules.already.exists.for.component");
		}
		Long ruleId = ruleRepository.insert(ruleVO);
		RuleVO insertedRule = ruleRepository.findById(ruleId);
		if(insertedRule==null){
			throw new UnknownResourceException("rule.not.found");
		}
		return insertedRule;
	}

	@Override
	public RuleVO read(Long id) {
		RuleVO rule = ruleRepository.findById(id);
		if(rule==null){
			throw new UnknownResourceException("rule.not.found");
		}
		return rule;
	}

	@Override
	public void update(RuleVO ruleVO) {
		RuleVO rule = ruleRepository.findById(ruleVO.getId());
		if(rule==null){
			throw new UnknownResourceException("rule.not.found");
		}
		ruleRepository.update(ruleVO);
	}

	@Override
	public void delete(Long id) {
		RuleVO rule = ruleRepository.findById(id);
		if(rule==null){
			throw new UnknownResourceException("rule.not.found");
		}
		ruleRepository.delete(id);
	}

	public List<RulePropertyVO> readAllProperties(Long id){
		return ruleRepository.findById(id).getProperties();
	}
	
}
