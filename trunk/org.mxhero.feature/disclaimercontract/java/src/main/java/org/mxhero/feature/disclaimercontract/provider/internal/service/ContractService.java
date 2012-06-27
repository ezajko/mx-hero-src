package org.mxhero.feature.disclaimercontract.provider.internal.service;

import java.util.Calendar;
import java.util.List;

import org.mxhero.feature.disclaimercontract.provider.internal.entity.Approval;


public interface ContractService {

	boolean isApproved(Long ruleId, String recipientId);
	
	void approve(Long ruleId, String recipientId);

	List<Approval> findNotificantionPending(Calendar since);
	
}
