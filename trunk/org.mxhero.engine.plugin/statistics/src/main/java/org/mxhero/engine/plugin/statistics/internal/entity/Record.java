package org.mxhero.engine.plugin.statistics.internal.entity;

import java.sql.Timestamp;

/**
 * Represents an email. One email may have to record in a database, one for each phase.
 * @author mmarmol
 */
public class Record{

	private Timestamp insertDate;
	
	private Long sequence;

	private String serverName;
	
	private String messageId;

	private String phase;

	private String sender;

	private String senderId;
	
	private String recipient;
	
	private String recipientId;

	private String senderDomainId;
	
	private String recipientDomainId;
	
	private String subject;

	private String from;

	private String toRecipients;

	private String ccRecipients;

	private String bccRecipients;

	private String ngRecipients;

	private Integer bytesSize;

	private String state;

	private String stateReason;

	private String flow;
	
	private String senderGroup;
	
	private String recipientGroup;

	public Timestamp getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Timestamp insertDate) {
		this.insertDate = insertDate;
	}

	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}

	public String getSenderDomainId() {
		return senderDomainId;
	}

	public void setSenderDomainId(String senderDomainId) {
		this.senderDomainId = senderDomainId;
	}

	public String getRecipientDomainId() {
		return recipientDomainId;
	}

	public void setRecipientDomainId(String recipientDomainId) {
		this.recipientDomainId = recipientDomainId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getToRecipients() {
		return toRecipients;
	}

	public void setToRecipients(String toRecipients) {
		this.toRecipients = toRecipients;
	}

	public String getCcRecipients() {
		return ccRecipients;
	}

	public void setCcRecipients(String ccRecipients) {
		this.ccRecipients = ccRecipients;
	}

	public String getBccRecipients() {
		return bccRecipients;
	}

	public void setBccRecipients(String bccRecipients) {
		this.bccRecipients = bccRecipients;
	}

	public String getNgRecipients() {
		return ngRecipients;
	}

	public void setNgRecipients(String ngRecipients) {
		this.ngRecipients = ngRecipients;
	}

	public Integer getBytesSize() {
		return bytesSize;
	}

	public void setBytesSize(Integer bytesSize) {
		this.bytesSize = bytesSize;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStateReason() {
		return stateReason;
	}

	public void setStateReason(String stateReason) {
		this.stateReason = stateReason;
	}

	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

	public String getSenderGroup() {
		return senderGroup;
	}

	public void setSenderGroup(String senderGroup) {
		this.senderGroup = senderGroup;
	}

	public String getRecipientGroup() {
		return recipientGroup;
	}

	public void setRecipientGroup(String recipientGroup) {
		this.recipientGroup = recipientGroup;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((insertDate == null) ? 0 : insertDate.hashCode());
		result = prime * result
				+ ((sequence == null) ? 0 : sequence.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Record other = (Record) obj;
		if (insertDate == null) {
			if (other.insertDate != null)
				return false;
		} else if (!insertDate.equals(other.insertDate))
			return false;
		if (sequence == null) {
			if (other.sequence != null)
				return false;
		} else if (!sequence.equals(other.sequence))
			return false;
		return true;
	}

}
