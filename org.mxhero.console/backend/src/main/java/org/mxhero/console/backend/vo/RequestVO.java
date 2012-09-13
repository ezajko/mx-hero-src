/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mxhero.console.backend.vo;

import java.util.Calendar;

public class RequestVO {

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
		builder.append("RequestVO [id=").append(id).append(", recipient=")
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
