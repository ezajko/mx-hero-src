package org.mxhero.feature.disclaimercontract.provider.internal.repository;

import java.util.List;

import org.mxhero.feature.disclaimercontract.provider.internal.entity.Approval;

public interface ContractRepository {
	
	Approval save(Approval aproval);
	
	List<Approval> findByRule(Long rule, String type);
	
	Approval findByRuleAndRecipient(Long rule, String recipient, String type);
	
}
