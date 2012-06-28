package org.mxhero.engine.plugin.disclaimercontract.internal.service;

import java.util.Calendar;
import java.util.List;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.plugin.disclaimercontract.entity.Contract;
import org.mxhero.engine.plugin.disclaimercontract.service.ContractService;

public class JdbcContractService implements ContractService{

	@Override
	public boolean isApproved(Long ruleId, String recipientId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void requestApproval(Long ruleId, String disclaimerPlain,
			String disclaimerHtml, MimeMail mail) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Contract> findNotificantionPending(Calendar since) {
		// TODO Auto-generated method stub
		return null;
	}

}
