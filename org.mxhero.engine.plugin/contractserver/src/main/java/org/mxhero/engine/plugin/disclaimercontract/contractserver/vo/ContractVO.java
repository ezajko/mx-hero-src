package org.mxhero.engine.plugin.disclaimercontract.contractserver.vo;

import java.util.Calendar;

public class ContractVO {

	private Long id;
	private String recipient;
	private String senderDomain;
	private Long ruleId;
	private Calendar approvedDate;
	private String disclaimerPlain;
	private String disclaimerHtml;
	private String aditionalData;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getRecipient() {
		return recipient;
	}
	
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	
	public String getSenderDomain() {
		return senderDomain;
	}
	
	public void setSenderDomain(String senderDomain) {
		this.senderDomain = senderDomain;
	}

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	public Calendar getApprovedDate() {
		return approvedDate;
	}
	
	public void setApprovedDate(Calendar approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getDisclaimerPlain() {
		return disclaimerPlain;
	}

	public void setDisclaimerPlain(String disclaimerPlain) {
		this.disclaimerPlain = disclaimerPlain;
	}

	public String getDisclaimerHtml() {
		return disclaimerHtml;
	}

	public void setDisclaimerHtml(String disclaimerHtml) {
		this.disclaimerHtml = disclaimerHtml;
	}

	public String getAditionalData() {
		return aditionalData;
	}

	public void setAditionalData(String aditionalData) {
		this.aditionalData = aditionalData;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContractVO [id=").append(id).append(", recipient=")
				.append(recipient).append(", senderDomain=")
				.append(senderDomain).append(", ruleId=").append(ruleId)
				.append(", approvedDate=").append(approvedDate)
				.append(", aditionalData=").append(aditionalData).append("]");
		return builder.toString();
	}

}
