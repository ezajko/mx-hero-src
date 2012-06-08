package org.mxhero.webapi.service;

import java.util.List;

import org.mxhero.webapi.vo.RuleVO;

public interface RuleService {

	public List<RuleVO> readAll(String domain, String component);
	
	public RuleVO create(RuleVO ruleVO);
	
	public RuleVO read(Long id);
	
	public void update(RuleVO ruleVO);

	public void delete(Long id);
	
}
