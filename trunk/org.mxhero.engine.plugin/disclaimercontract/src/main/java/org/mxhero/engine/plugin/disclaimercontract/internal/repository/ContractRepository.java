package org.mxhero.engine.plugin.disclaimercontract.internal.repository;

import java.util.List;

import org.mxhero.engine.plugin.disclaimercontract.entity.Contract;

public interface ContractRepository {
	
	List<Contract> findByRule(Long rule, String type);
	
	Contract findByRuleAndRecipient(Long rule, String recipient, String type);
	
}
