package org.mxhero.engine.plugin.threadlight.internal.vo;

import java.sql.Timestamp;

public class ThreadRow {

	private String messageId;
	private String senderMail;
	private Timestamp creationTime;
	private String subject;
	private String recipientMail;
	private Timestamp replyTime;
	
	public String getMessageId() {
		return messageId;
	}
	
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	public String getSenderMail() {
		return senderMail;
	}

	public void setSenderMail(String senderMail) {
		this.senderMail = senderMail;
	}
	
	public Timestamp getCreationTime() {
		return creationTime;
	}
	
	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getRecipientMail() {
		return recipientMail;
	}
	
	public void setRecipientMail(String recipientMail) {
		this.recipientMail = recipientMail;
	}

	public Timestamp getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Timestamp replyTime) {
		this.replyTime = replyTime;
	}

}
