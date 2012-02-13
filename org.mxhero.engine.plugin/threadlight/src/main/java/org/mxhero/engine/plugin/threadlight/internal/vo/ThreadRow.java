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

}
