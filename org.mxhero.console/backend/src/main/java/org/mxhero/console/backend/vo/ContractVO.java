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
