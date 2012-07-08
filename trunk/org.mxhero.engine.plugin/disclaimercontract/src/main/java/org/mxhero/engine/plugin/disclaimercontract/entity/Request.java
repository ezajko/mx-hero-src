package org.mxhero.engine.plugin.disclaimercontract.entity;

import java.util.Calendar;

public class Request {

	public static final String CONTRACT_TYPE = "contract";
	public static final String ONE_TYPE = "one";
	
	private Long id;
	private String recipient;
	private String senderDomain;
	private Long ruleId;
	private Calendar approvedDate;
	private String disclaimerPlain;
	private String disclaimerHtml;
	private String additionalData;
	private String path;
	private Calendar requestDate;
	private Calendar vetoDate;
	private Boolean pending;
	private String messageId;
	private String type;
	private String phase;
	private Integer rulePriority;
	
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
	
	public String getAdditionalData() {
		return additionalData;
	}
	
	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public Calendar getRequestDate() {
		return requestDate;
	}
	
	public void setRequestDate(Calendar requestDate) {
		this.requestDate = requestDate;
	}
	
	public Calendar getVetoDate() {
		return vetoDate;
	}
	
	public void setVetoDate(Calendar vetoDate) {
		this.vetoDate = vetoDate;
	}

	public Boolean getPending() {
		return pending;
	}

	public void setPending(Boolean pending) {
		this.pending = pending;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public Integer getRulePriority() {
		return rulePriority;
	}

	public void setRulePriority(Integer rulePriority) {
		this.rulePriority = rulePriority;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Request [id=").append(id).append(", recipient=")
				.append(recipient).append(", senderDomain=")
				.append(senderDomain).append(", ruleId=").append(ruleId)
				.append(", approvedDate=").append(approvedDate)
				.append(", additionalData=").append(additionalData)
				.append(", path=").append(path).append(", requestDate=")
				.append(requestDate).append(", vetoDate=").append(vetoDate)
				.append(", pending=").append(pending).append(", messageId=")
				.append(messageId).append(", type=").append(type).append("]");
		return builder.toString();
	}

}
