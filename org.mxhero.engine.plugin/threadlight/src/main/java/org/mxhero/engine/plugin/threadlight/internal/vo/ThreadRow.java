package org.mxhero.engine.plugin.threadlight.internal.vo;

import java.sql.Timestamp;
import java.util.Set;

public class ThreadRow {

	private Long id;
	private String messageId;
	private String senderMail;
	private Timestamp creationTime;
	private String subject;
	private String recipientMail;
	private Timestamp replyTime;
	private Set<ThreadRowFollower> followers;

	
	public ThreadRow() {
	}

	public ThreadRow(String messageId, String senderMail, String recipientMail) {
		this.messageId = messageId;
		this.senderMail = senderMail;
		this.recipientMail = recipientMail;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Set<ThreadRowFollower> getFollowers() {
		return followers;
	}

	public void setFollowers(Set<ThreadRowFollower> followers) {
		this.followers = followers;
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
		ThreadRow other = (ThreadRow) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ThreadRow [id=").append(id).append(", messageId=")
				.append(messageId).append(", senderMail=").append(senderMail)
				.append(", creationTime=").append(creationTime)
				.append(", subject=").append(subject)
				.append(", recipientMail=").append(recipientMail)
				.append(", replyTime=").append(replyTime)
				.append(", followers=").append(followers).append("]");
		return builder.toString();
	}

}
