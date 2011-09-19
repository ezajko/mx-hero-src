package org.mxhero.engine.domain.mail;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
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

	private int initialSize;

	private Map<String, String> properties = new HashMap<String, String>();
	
	private StaticIdMimeMessage message;
	
	private String messageId;

	private String phase = RulePhase.SEND;

	private String status = MailState.DELIVER;
	
	private String flow = MailFlow.NONE;

	private String statusReason;

	public static MimeMail createCustom(String from, String recipient, InputStream data,
			String responseServiceId,Long sequence, Timestamp time) throws MessagingException{
		return new MimeMail(from, recipient, data, responseServiceId, sequence, time);
	}
	
	private MimeMail(String from, String recipient, InputStream data,
			String responseServiceId, Long sequence, Timestamp time) throws MessagingException{
		
		this.sequence = sequence;
		this.time = time;
		this.responseServiceId = responseServiceId;
		this.recipient = recipient;
		this.recipientId = recipient;
		this.recipientDomainId = getDomain(recipient);
		this.initialSender = from;
		this.senderId = from;
		this.senderDomainId = getDomain(from);
		try {
			this.message = new StaticIdMimeMessage(data);
			if(message.getMessageID()==null){
				this.messageId = message.generateAndGetMessageID();
			}else{
				this.messageId = message.getMessageID();
			}
		} catch (IOException e) {
			throw new MessagingException("error in inputstream",e);
		}
		this.messageId = message.getMessageID();
		this.initialSize = message.getSize();
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
			InputStream data, String responseServiceId)
			throws MessagingException {
		this(from, recipient, responseServiceId);

		try {
			this.message = new StaticIdMimeMessage(data);
			if(message.getMessageID()==null){
				this.messageId = message.generateAndGetMessageID();
			}else{
				this.messageId = message.getMessageID();
			}
		} catch (IOException e) {
			throw new MessagingException("error in inputstream",e);
		}

			

		this.initialSize = message.getSize();
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
		this.responseServiceId = responseServiceId;
		this.recipient = recipient;
		this.recipientId = recipient;
		this.recipientDomainId = getDomain(recipient);
		this.initialSender = from;
		this.senderId = from;
		this.senderDomainId = getDomain(from);
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

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	private static String getDomain(String email){
		if(email!=null && email.contains("@")){
			return email.split("@")[1];
		}else{
			return "";
		}
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((messageId == null) ? 0 : messageId.hashCode());
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
		MimeMail other = (MimeMail) obj;
		if (sequence == null) {
			if (other.sequence != null)
				return false;
		} else if (!sequence.equals(other.sequence))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MimeMail [sequence=").append(sequence)
				.append(", time=").append(time).append(", initialSender=")
				.append(initialSender).append(", recipient=").append(recipient)
				.append(", initialSize=").append(initialSize)
				.append(", phase=").append(phase).append(", status=")
				.append(status).append(", flow=").append(flow).append("]");
		return builder.toString();
	}
	

	private class StaticIdMimeMessage extends MimeMessage{
		
		public StaticIdMimeMessage(InputStream inputStream) throws MessagingException, IOException{
			super(Session.getDefaultInstance(new Properties()),inputStream);
		}
		
		public String generateAndGetMessageID() throws MessagingException{
			super.updateMessageID();
			return this.getMessageID();
		}
		
		@Override
		protected void updateMessageID() throws MessagingException {
			//if this message do not have an ID them create one.
			if(getMessageId()==null){
				super.updateMessageID();
			}else{
				//if there it has an id and the message has other, them override it.
				if(this.getMessageID()==null || !this.getMessageID().equals(getMessageId())){
					message.setHeader("Message-ID",getMessageId());
				}
			}
		}
	}
	
}
