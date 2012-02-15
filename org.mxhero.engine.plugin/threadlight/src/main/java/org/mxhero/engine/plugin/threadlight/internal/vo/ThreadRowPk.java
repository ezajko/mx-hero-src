package org.mxhero.engine.plugin.threadlight.internal.vo;

import java.io.Serializable;

public class ThreadRowPk implements Serializable{

	private static final long serialVersionUID = 2992486699615639710L;
	
	private String messageId;
	private String senderMail;
	private String recipientMail;
	
	public ThreadRowPk(){
	}
	
	public ThreadRowPk(String messageId, String senderMail, String recipientMail) {
		this.messageId = messageId;
		this.senderMail = senderMail;
		this.recipientMail = recipientMail;
	}

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
	
	public String getRecipientMail() {
		return recipientMail;
	}
	
	public void setRecipientMail(String recipientMail) {
		this.recipientMail = recipientMail;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((messageId == null) ? 0 : messageId.hashCode());
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
		ThreadRowPk other = (ThreadRowPk) obj;
		if (messageId == null) {
			if (other.messageId != null)
				return false;
		} else if (!messageId.equals(other.messageId))
			return false;
		if (recipientMail == null) {
			if (other.recipientMail != null)
				return false;
		} else if (!recipientMail.equals(other.recipientMail))
			return false;
		if (senderMail == null) {
			if (other.senderMail != null)
				return false;
		} else if (!senderMail.equals(other.senderMail))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ThreadRowPk [messageId=").append(messageId)
				.append(", senderMail=").append(senderMail)
				.append(", recipientMail=").append(recipientMail).append("]");
		return builder.toString();
	}
	
}
