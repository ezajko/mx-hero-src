package org.mxhero.engine.plugin.disclaimercontract.command;

import org.mxhero.engine.commons.mail.command.NamedParameters;

public class RequestContractApprovalParameters extends NamedParameters{
	
	public static final String RULE_ID = "ruleId";	
	public static final String SENDER_DOMAIN = "senderDomain";	
	public static final String RECIPIENT = "recipient";	
	public static final String DISCLAIMER_PLAIN = "disclaimerPlain";
	public static final String DISCLAIMER_HTML = "disclaimerHtml";
	public static final String APPROVED = "approved";
	public static final String RULE_PRIORITY = "rulePriority";
	public static final String REQUEST_ID = "requestId";
	
	public RequestContractApprovalParameters() {
	}

	public RequestContractApprovalParameters(Long ruleId, String disclaimerPlain, String disclaimerHtml) {
		this.setDisclaimerHtml(disclaimerHtml);
		this.setDisclaimerPlain(disclaimerPlain);
		this.setRuleId(ruleId);
		this.setApproved(false);
	}
	
	public RequestContractApprovalParameters(NamedParameters parameters){
		this.nameToInstance.putAll(parameters.getNameToInstance());
	}
	
	public Long getRuleId(){
		return get(RULE_ID);
	}
	
	public void setRuleId(Long ruleId){
		put(RULE_ID, ruleId);
	}
	
	public String getSenderDomain(){
		return get(SENDER_DOMAIN);
	}
	
	public void setSenderDomain(String senderDomain){
		put(SENDER_DOMAIN, senderDomain);
	}
	
	public String getRecipient(){
		return get(RECIPIENT);
	}
	
	public void setRecipient(String recipient){
		put(RECIPIENT, recipient);
	}
	
	public String getDisclaimerPlain(){
		return get(DISCLAIMER_PLAIN);
	}
	
	public void setDisclaimerPlain(String disclaimerPlain){
		put(DISCLAIMER_PLAIN, disclaimerPlain);
	}
	
	public String getDisclaimerHtml(){
		return get(DISCLAIMER_HTML);
	}
	
	public void setDisclaimerHtml(String disclaimerHtml){
		put(DISCLAIMER_HTML, disclaimerHtml);
	}

	public Boolean getApproved() {
		return get(APPROVED);
	}

	public void setApproved(Boolean approved) {
		put(APPROVED, approved);
	}

	public Integer getRulePriority(){
		return get(RULE_PRIORITY);
	}
	
	public void setRulePriority(Integer rulePriority) {
		put(RULE_PRIORITY, rulePriority);
	}
	
	public Long getRequestId(){
		return get(REQUEST_ID);
	}
	
	public void setRequestId(Long requestId) {
		put(REQUEST_ID, requestId);
	}
	
}
