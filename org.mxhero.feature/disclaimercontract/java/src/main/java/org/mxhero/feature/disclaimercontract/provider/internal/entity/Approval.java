package org.mxhero.feature.disclaimercontract.provider.internal.entity;

import java.util.Calendar;

public class Approval {

	private Long id;
	private String triggerMailMessageId;
	private String recipient;
	private String senderDomain;
	private Long ruleId;
	private Calendar approvedDate;
	private String type;
	private String disclaimerPlain;
	private String disclaimerHtml;
	private String aditionalData;
	private Calendar requestSentDate;
	private Calendar notificationSent;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTriggerMailMessageId() {
		return triggerMailMessageId;
	}

	public void setTriggerMailMessageId(String triggerMailMessageId) {
		this.triggerMailMessageId = triggerMailMessageId;
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
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
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

	public Calendar getRequestSentDate() {
		return requestSentDate;
	}
	
	public void setRequestSentDate(Calendar requestSentDate) {
		this.requestSentDate = requestSentDate;
	}

	public Calendar getNotificationSent() {
		return notificationSent;
	}

	public void setNotificationSent(Calendar notificationSent) {
		this.notificationSent = notificationSent;
	}
	
}
