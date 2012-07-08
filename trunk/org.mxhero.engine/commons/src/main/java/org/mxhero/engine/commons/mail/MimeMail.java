package org.mxhero.engine.commons.mail;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.mxhero.engine.commons.mail.api.Mail;


/**
 * Entity that represents the mail that is been processed in the platform, this
 * class is common for all modules.
 * 
 * @author mmarmol
 */
public final class MimeMail {

	private Long sequence;

	private Timestamp time;

	private String sender;

	private String recipient;
	
	private String senderId;
	
	private String senderDomainId;

	private String recipientId;
	
	private String recipientDomainId;
	
	private String senderGroup;
	
	private String recipientGroup;

	private String responseServiceId;

	private long initialSize;
	
	private int deliverTries;

	private Map<String, String> properties = new HashMap<String, String>();
	
	private StaticIdMimeMessage message;
	
	private String messageId;

	private Mail.Phase phase = Mail.Phase.send;

	private Mail.Status status = Mail.Status.deliver;
	
	private Mail.Flow flow = Mail.Flow.none;

	private String statusReason;
	
	private Mail bussinesObject;
	
	private Long forcedPhasePriority;

	public static MimeMail createCustom(String from, String recipient, InputStream data,
			String responseServiceId,Long sequence, Timestamp time) throws MessagingException{
		return new MimeMail(from, recipient, data, responseServiceId, sequence, time);
	}
	
	private MimeMail(String from, String recipient, InputStream data,
			String responseServiceId, Long sequence, Timestamp time) throws MessagingException{
		
		this.sequence = sequence;
		this.time = time;
		this.responseServiceId = responseServiceId;
		this.recipient = toLower(recipient);
		this.recipientId = toLower(recipient);
		this.recipientDomainId = getDomain(recipient);
		this.sender = toLower(from);
		this.senderId = toLower(from);
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
		this.recipient = toLower(recipient);
		this.recipientId = toLower(recipient);
		this.recipientDomainId = getDomain(recipient);
		this.sender = toLower(from);
		this.senderId = toLower(from);
		this.senderDomainId = getDomain(from);
	}

	/**
	 * @return
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @return
	 */
	public String getRecipient() {
		return recipient;
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
	public Mail.Phase getPhase() {
		return phase;
	}

	/**
	 * @param phase
	 */
	public void setPhase(Mail.Phase phase) {
		this.phase = phase;
		this.forcedPhasePriority = null;
	}

	/**
	 * @return the initialSize
	 */
	public long getInitialSize() {
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
	public Mail.Status getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Mail.Status status) {
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

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
	public Mail.Flow getFlow() {
		return flow;
	}

	public void setFlow(Mail.Flow flow) {
		this.flow = flow;
	}

	public Long getForcedPhasePriority() {
		return forcedPhasePriority;
	}

	public void setForcedPhasePriority(Long forcedPhasePriority) {
		this.forcedPhasePriority = forcedPhasePriority;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
		if(message!=null){
			try {
				message.updateMessageID();
			} catch (MessagingException e) {}
		}
	}

	private static String getDomain(String email){
		if(email!=null && email.contains("@")){
			return email.split("@")[1].trim().toLowerCase();
		}else{
			return "";
		}
	}
	
	private static String toLower(String value){
		if(value == null){
			return null;
		}
		return value.trim().toLowerCase();
	}

	public int getDeliverTries() {
		return deliverTries;
	}

	public void setDeliverTries(int deliverTries) {
		this.deliverTries = deliverTries;
	}
	
	public Mail getBussinesObject() {
		return bussinesObject;
	}

	public void setBussinesObject(Mail bussinesObject) {
		this.bussinesObject = bussinesObject;
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
				.append(", time=").append(time).append(", sender=")
				.append(sender).append(", recipient=").append(recipient)
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
