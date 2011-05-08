package org.mxhero.engine.domain.mail;

import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.mxhero.engine.domain.mail.business.MailFlow;
import org.mxhero.engine.domain.mail.business.MailState;
import org.mxhero.engine.domain.mail.business.RulePhase;

/**
 * Entity that represents the mail that is been processed in the platform, this
 * class is common for all modules.
 * 
 * @author mmarmol
 */
public final class MimeMail {

	private Long sequence;

	private Timestamp time;

	private String initialSender;

	private String recipient;
	
	private String senderId;
	
	private String senderDomainId;

	private String recipientId;
	
	private String recipientDomainId;

	private String responseServiceId;
	
	private Long parentSequence;

	private Timestamp parentTime;

	private int initialSize;

	private Map<String, String> properties = new HashMap<String, String>();
	
	private MimeMessage message;

	private String phase = RulePhase.SEND;

	private String status = MailState.DELIVER;
	
	private String flow = MailFlow.NONE;

	private String statusReason;

	/**
	 * @param from
	 * @param recipient
	 * @param data
	 * @param responseServiceId
	 * @throws MessagingException
	 */
	public MimeMail(String from, String recipient, byte[] data,
			String responseServiceId) throws MessagingException {
		this(from, recipient, responseServiceId);
		this.initialSize = data.length;
		this.message = new MimeMessage(Session
				.getDefaultInstance(new Properties()),
				new ByteArrayInputStream(data));
	}

	/**
	 * Clones for default
	 * @param from
	 * @param recipients
	 * @param data
	 * @param responseServiceId
	 * @throws MessagingException
	 */
	public MimeMail(String from, String recipient,
			MimeMessage data, String responseServiceId)
			throws MessagingException {
		this(from,recipient,data,responseServiceId,true);
	}

	/**
	 * @param from
	 * @param recipients
	 * @param data
	 * @param responseServiceId
	 * @param clone
	 * @throws MessagingException
	 */
	public MimeMail(String from, String recipient,
			MimeMessage data, String responseServiceId, Boolean clone)
			throws MessagingException {
		this(from, recipient, responseServiceId);
		int headerSize = 0;
		@SuppressWarnings("rawtypes")
		Enumeration e = data.getAllHeaderLines();
		if (e.hasMoreElements()) {
			headerSize += 2;
		}
		while (e.hasMoreElements()) {
			// add 2 bytes for the CRLF
			headerSize += ((String) e.nextElement()).length() + 2;
		}
		this.initialSize = data.getSize() + headerSize;
		if(clone){
			this.message = new MimeMessage(data);
		}else{
			this.message = data;
		}
	}
	
	/**
	 * @param from
	 * @param recipients
	 * @param responseServiceId
	 */
	private MimeMail(String from, String recipient,
			String responseServiceId) {
		this.sequence = Sequencer.getInstance().getNextSequence();
		this.time = new Timestamp(System.currentTimeMillis());
		this.parentSequence = this.sequence;
		this.parentTime = this.time;
		this.responseServiceId = responseServiceId;
		this.recipient = recipient;
		this.initialSender = from;
	}

	/**
	 * @return
	 */
	public String getInitialSender() {
		return initialSender;
	}

	/**
	 * @return
	 */
	public String getRecipient() {
		return recipient;
	}

	/**
	 * @param recipient
	 */
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	/**
	 * @return
	 */
	public MimeMessage getMessage() {
		return message;
	}

	/**
	 * @return
	 */
	public String getResponseServiceId() {
		return responseServiceId;
	}

	/**
	 * @return
	 */
	public String getPhase() {
		return phase;
	}

	/**
	 * @param phase
	 */
	public void setPhase(String phase) {
		this.phase = phase;
	}

	/**
	 * @return the initialSize
	 */
	public int getInitialSize() {
		return initialSize;
	}

	/**
	 * @return the sequence
	 */
	public Long getSequence() {
		return sequence;
	}

	/**
	 * @return the time
	 */
	public Timestamp getTime() {
		return time;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the statusReason
	 */
	public String getStatusReason() {
		return statusReason;
	}

	/**
	 * @param statusReason
	 *            the statusReason to set
	 */
	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
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

	public Long getParentSequence() {
		return parentSequence;
	}

	public void setParentSequence(Long parentSequence) {
		this.parentSequence = parentSequence;
	}

	public Timestamp getParentTime() {
		return parentTime;
	}

	public void setParentTime(Timestamp parentTime) {
		this.parentTime = parentTime;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MimeMail [time=").append(time).append(", sequence=")
				.append(sequence).append(", phase=").append(phase).append(
						", status=").append(status).append(", statusReason=")
				.append(statusReason).append(", initialSender=").append(
						initialSender).append(", recipient=").append(recipient)
				.append(", initialSize=").append(initialSize).append(
						", responseServiceId=").append(responseServiceId)
				.append(", senderId=").append(senderId).append(
						", senderDomainId=").append(senderDomainId).append(
						", recipientId=").append(recipientId).append(
						", recipientDomainId=").append(recipientDomainId)
				.append("]");
		return builder.toString();
	}

}
