package org.mxhero.engine.plugin.disclaimercontract.internal.command;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.command.NamedParameters;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.plugin.disclaimercontract.command.RequestContractApproval;
import org.mxhero.engine.plugin.disclaimercontract.command.RequestContractApprovalParameters;
import org.mxhero.engine.plugin.disclaimercontract.entity.Request;
import org.mxhero.engine.plugin.disclaimercontract.service.ContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestContractApprovalImpl implements RequestContractApproval{

	private static Logger log = LoggerFactory.getLogger(RequestContractApprovalImpl.class);
	private ContractService service;
	
	@Override
	public Result exec(MimeMail mail, NamedParameters parameters) {
		RequestContractApprovalParameters rcaParameters = new RequestContractApprovalParameters(parameters);
		Result result = new Result();
		result.setConditionTrue(false);
		result.setParameters(rcaParameters);
		String recipient =rcaParameters.getRecipient();
		String senderDomain = rcaParameters.getSenderDomain();

		if(rcaParameters.getRuleId()==null){
			result.setAnError(true);
			result.setMessage("rule id is null");
			return result;
		}
		if(rcaParameters.getDisclaimerPlain()==null){
			result.setAnError(true);
			result.setMessage("disclaimer plain is null");
			return result;
		}
		if(rcaParameters.getDisclaimerHtml()==null){
			result.setAnError(true);
			result.setMessage("disclaimer html is null");
			return result;
		}
		
		if(senderDomain==null || senderDomain.trim().isEmpty()){
			log.debug("sender is null");
			rcaParameters.setSenderDomain(mail.getSenderDomainId());
			senderDomain=mail.getSenderDomainId();
		}
		if(recipient==null || recipient.trim().isEmpty()){
			log.debug("recipient is null");
			rcaParameters.setRecipient(mail.getRecipientId());
			recipient=mail.getRecipientId();
		}
		
		
		try{
			rcaParameters.setApproved(service.isApproved(rcaParameters.getRuleId(), recipient));
			if(!rcaParameters.getApproved()){
				Request request = new Request();
				request.setDisclaimerHtml(rcaParameters.getDisclaimerHtml());
				request.setDisclaimerPlain(rcaParameters.getDisclaimerPlain());
				request.setRecipient(rcaParameters.getRecipient());
				request.setRuleId(rcaParameters.getRuleId());
				request.setSenderDomain(rcaParameters.getSenderDomain());
				request.setRulePriority(rcaParameters.getRulePriority());
				request = service.request(request, mail);
				rcaParameters.setRequestId(request.getId());
			}
		}catch(Exception e){
			result.setAnError(true);
			result.setMessage("service error:"+e.getMessage());
			return result;
		}
		result.setConditionTrue(true);
		return result;
	}

	public ContractService getService() {
		return service;
	}

	public void setService(ContractService service) {
		this.service = service;
	}

}
