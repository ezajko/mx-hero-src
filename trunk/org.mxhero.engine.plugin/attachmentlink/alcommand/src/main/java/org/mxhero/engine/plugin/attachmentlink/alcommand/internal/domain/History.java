package org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain;

import java.sql.Timestamp;

/**
 * @author royojp
 * @version 1.0
 * @created 22-Aug-2011 12:52:23
 */
public class History {

	private Long id;
	private boolean couldDownload;
	private Timestamp accessTime;
	private MessageAttachRecipient messageAttachmentRecipient;

	public History(){
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isCouldDownload() {
		return couldDownload;
	}

	public void setCouldDownload(boolean couldDownload) {
		this.couldDownload = couldDownload;
	}

	public Timestamp getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(Timestamp accessTime) {
		this.accessTime = accessTime;
	}

	public MessageAttachRecipient getMessageAttachmentRecipient() {
		return messageAttachmentRecipient;
	}

	public void setMessageAttachmentRecipient(
			MessageAttachRecipient messageAttachmentRecipient) {
		this.messageAttachmentRecipient = messageAttachmentRecipient;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		History other = (History) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}