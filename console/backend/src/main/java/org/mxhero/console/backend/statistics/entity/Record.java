package org.mxhero.console.backend.statistics.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * Represents an email. One email may have to record in a database, one for each phase.
 * @author mmarmol
 */
@Entity
@Table(name = "mail_records",schema="statistics")
public class Record implements Serializable{

	private static final long serialVersionUID = -4673170035825892441L;

	@EmbeddedId
	private RecordPk id;

	@Column(name = "message_id", length = 1024, nullable=false)
	private String messageId;
	
	@Column(name="parent_insert_date")
	private Timestamp parentInsertDate;
	
	@Column(name="parent_sequence")
	private Long parentSequence;

	@Column(name = "phase", length = 10, nullable=false)
	private String phase;

	@Column(name = "sender", length = 50)
	private String sender;

	@Column(name = "sender_id", length = 50)
	private String senderId;
	
	@Column(name = "recipient", length = 50)
	private String recipient;
	
	@Column(name = "recipient_id", length = 50)
	private String recipientId;

	@Column(name = "sender_domain_id", length = 50)
	private String senderDomainId;
	
	@Column(name = "recipient_domain_id", length = 50)
	private String recipientDomainId;
	
	@Column(name = "subject")
	private String subject;

	@Column(name = "from_recipeints", length = 1024)
	private String from;

	@Column(name = "to_recipeints", length = 1024)
	private String toRecipients;

	@Column(name = "cc_recipeints", length = 1024)
	private String ccRecipients;

	@Column(name = "bcc_recipeints", length = 1024)
	private String bccRecipients;

	@Column(name = "ng_recipeints", length = 1024)
	private String ngRecipients;

	@Column(name = "sent_date")
	private Calendar sentDate;

	@Column(name = "bytes_size")
	private Integer bytesSize;

	@Column(name = "state")
	private String state;

	@Column(name = "state_reason")
	private String stateReason;

	@Column(name = "flow", length = 10)
	private String flow;

	/**
	 * @return
	 */
	public RecordPk getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(RecordPk id) {
		this.id = id;
	}

	/**
	 * @return the messageId
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * @param messageId
	 *            the messageId to set
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * @return the phase
	 */
	public String getPhase() {
		return phase;
	}

	/**
	 * @param phase
	 *            the phase to set
	 */
	public void setPhase(String phase) {
		this.phase = phase;
	}

	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @param sender
	 *            the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * @return the recipient
	 */
	public String getRecipient() {
		return recipient;
	}

	/**
	 * @param recipient
	 *            the recipient to set
	 */
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	

	/**
	 * @return the senderId
	 */
	public String getSenderId() {
		return senderId;
	}

	/**
	 * @param senderId the senderId to set
	 */
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	
	/**
	 * @return the recipientId
	 */
	public String getRecipientId() {
		return recipientId;
	}

	/**
	 * @param recipientId the recipientId to set
	 */
	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}

	/**
	 * @return the senderDomainId
	 */
	public String getSenderDomainId() {
		return senderDomainId;
	}

	/**
	 * @param senderDomainId the senderDomainId to set
	 */
	public void setSenderDomainId(String senderDomainId) {
		this.senderDomainId = senderDomainId;
	}
	
	/**
	 * @return the recipientDomainId
	 */
	public String getRecipientDomainId() {
		return recipientDomainId;
	}

	/**
	 * @param recipientDomainId the recipientDomainId to set
	 */
	public void setRecipientDomainId(String recipientDomainId) {
		this.recipientDomainId = recipientDomainId;
	}

	/**
	 * @return
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the toRecipients
	 */
	public String getToRecipients() {
		return toRecipients;
	}

	/**
	 * @param toRecipients
	 *            the toRecipients to set
	 */
	public void setToRecipients(String toRecipients) {
		this.toRecipients = toRecipients;
	}

	/**
	 * @return the ccRecipients
	 */
	public String getCcRecipients() {
		return ccRecipients;
	}

	/**
	 * @param ccRecipients
	 *            the ccRecipients to set
	 */
	public void setCcRecipients(String ccRecipients) {
		this.ccRecipients = ccRecipients;
	}

	/**
	 * @return the bccRecipients
	 */
	public String getBccRecipients() {
		return bccRecipients;
	}

	/**
	 * @param bccRecipients
	 *            the bccRecipients to set
	 */
	public void setBccRecipients(String bccRecipients) {
		this.bccRecipients = bccRecipients;
	}

	/**
	 * @return the ngRecipients
	 */
	public String getNgRecipients() {
		return ngRecipients;
	}

	/**
	 * @param ngRecipients
	 *            the ngRecipients to set
	 */
	public void setNgRecipients(String ngRecipients) {
		this.ngRecipients = ngRecipients;
	}

	/**
	 * @return the sentDate
	 */
	public Calendar getSentDate() {
		return sentDate;
	}

	/**
	 * @param sentDate
	 *            the sentDate to set
	 */
	public void setSentDate(Calendar sentDate) {
		this.sentDate = sentDate;
	}

	/**
	 * @return the bytesSize
	 */
	public Integer getBytesSize() {
		return bytesSize;
	}

	/**
	 * @param bytesSize
	 *            the bytesSize to set
	 */
	public void setBytesSize(Integer bytesSize) {
		this.bytesSize = bytesSize;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the stateReason
	 */
	public String getStateReason() {
		return stateReason;
	}

	/**
	 * @param stateReason the stateReason to set
	 */
	public void setStateReason(String stateReason) {
		this.stateReason = stateReason;
	}

	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

	public Timestamp getParentInsertDate() {
		return parentInsertDate;
	}

	public void setParentInsertDate(Timestamp parentInsertDate) {
		this.parentInsertDate = parentInsertDate;
	}

	public Long getParentSequence() {
		return parentSequence;
	}

	public void setParentSequence(Long parentSequence) {
		this.parentSequence = parentSequence;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Record)) {
			return false;
		}
		Record other = (Record) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Record [id=").append(id).append(", phase=")
				.append(phase).append(", sender=").append(sender)
				.append(", recipient=").append(recipient).append(", sentDate=")
				.append(sentDate).append(", state=").append(state)
				.append(", flow=").append(flow).append("]");
		return builder.toString();
	}

}
