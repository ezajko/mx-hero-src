package org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author royojp
 * @version 1.0
 * @created 22-Aug-2011 12:52:23
 */
public class MessageAttachRecipient {

	private Long id;
	private String recipient;
	private boolean enableToDownload;
	private Attach attach;
	private Message message;
	private boolean wasAccessFirstTime;
	private Timestamp creationDate;
	private List<History> history;


	public MessageAttachRecipient(){
		this.history = new ArrayList<History>();
	}

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

	public boolean isEnableToDownload() {
		return enableToDownload;
	}

	public void setEnableToDownload(boolean enableToDownload) {
		this.enableToDownload = enableToDownload;
	}

	public Attach getAttach() {
		return attach;
	}

	public void setAttach(Attach attach) {
		this.attach = attach;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public List<History> getHistory() {
		return history;
	}

	public void setHistory(List<History> history) {
		this.history = history;
	}

	public void setWasAccessFirstTime(boolean wasAccessFirstTime) {
		this.wasAccessFirstTime = wasAccessFirstTime;
	}

	public boolean isWasAccessFirstTime() {
		return wasAccessFirstTime;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attach == null) ? 0 : attach.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result
				+ ((recipient == null) ? 0 : recipient.hashCode());
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
		MessageAttachRecipient other = (MessageAttachRecipient) obj;
		if (attach == null) {
			if (other.attach != null)
				return false;
		} else if (!attach.equals(other.attach))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (recipient == null) {
			if (other.recipient != null)
				return false;
		} else if (!recipient.equals(other.recipient))
			return false;
		return true;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Recipient: ");
		builder.append(getRecipient());
		return builder.toString();
	};

}