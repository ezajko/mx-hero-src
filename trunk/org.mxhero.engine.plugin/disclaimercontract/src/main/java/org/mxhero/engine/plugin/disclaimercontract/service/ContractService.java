package org.mxhero.engine.plugin.disclaimercontract.service;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.plugin.disclaimercontract.entity.Contract;
import org.mxhero.engine.plugin.disclaimercontract.entity.Request;


public interface ContractService {

	boolean isApproved(Long ruleId, String recipient);
	
	Request request(Request request, MimeMail mail);
	
	Contract sign(Request request);
	
}
