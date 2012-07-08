package org.mxhero.engine.plugin.disclaimercontract.internal.repository;

import java.util.List;

import org.mxhero.engine.plugin.disclaimercontract.entity.Contract;
import org.mxhero.engine.plugin.disclaimercontract.entity.Request;

public interface ContractRepository {
	
	List<Contract> findByRule(Long ruleId);
	
	Contract findByRuleAndRecipient(Long ruleId, String recipient);
	
	Request addRequest(Request request);
	
	public List<Request> vetoRequests();
	
	List<Request> pending();
	
	List<Request> oldNotAccepted(Integer hours);
	
	void remove(Long requestId);
	
	Contract create(Contract contract);
	
	void markDone(Long requestId);
	
}
