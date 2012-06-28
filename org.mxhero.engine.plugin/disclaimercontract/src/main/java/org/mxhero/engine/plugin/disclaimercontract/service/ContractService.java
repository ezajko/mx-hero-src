package org.mxhero.engine.plugin.disclaimercontract.service;

import java.util.Calendar;
import java.util.List;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.plugin.disclaimercontract.entity.Contract;


public interface ContractService {

	boolean isApproved(Long ruleId, String recipientId);
	
	void requestApproval(Long ruleId, String disclaimerPlain, String disclaimerHtml, MimeMail mail);

	List<Contract> findNotificantionPending(Calendar since);
	
}
