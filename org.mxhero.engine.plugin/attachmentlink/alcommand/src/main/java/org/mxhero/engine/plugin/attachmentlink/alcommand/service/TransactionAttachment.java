package org.mxhero.engine.plugin.attachmentlink.alcommand.service;

import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * The Class TransactionAttachment.
 */
public class TransactionAttachment {
	
	/** The id message attach. */
	private Long idMessageAttach;
	
	/** The email. */
	private String email;
	
	/** The file path. */
	private String filePath;
	
	/** The original file name. */
	private String originalFileName;
	
	/** The public url. */
	private String publicUrl;
	
	/** The subject. */
	private String subject;
	
	/** The sender. */
	private String sender;
	
	/** The recipient. */
	private String recipient;
	
	/** The email date. */
	private Timestamp emailDate;
	
	/** The is sender. */
	private boolean isSender;
	
	/** The is recipient. */
	private boolean isRecipient;

	
	/**
	 * Sets the public url.
	 *
	 * @param publicUrl the new public url
	 */
	public void setPublicUrl(String publicUrl) {
		this.publicUrl = publicUrl;
	}
	
	/**
	 * Gets the public url.
	 *
	 * @return the public url
	 */
	public String getPublicUrl() {
		return publicUrl;
	}
	
	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Gets the file path.
	 *
	 * @return the file path
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Sets the file path.
	 *
	 * @param filePath the new file path
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Gets the id message attach.
	 *
	 * @return the id message attach
	 */
	public Long getIdMessageAttach() {
		return idMessageAttach;
	}

	/**
	 * Sets the id message attach.
	 *
	 * @param idMessageAttach the new id message attach
	 */
	public void setIdMessageAttach(Long idMessageAttach) {
		this.idMessageAttach = idMessageAttach;
	}
	
	/**
	 * Gets the subject.
	 *
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets the subject.
	 *
	 * @param subject the new subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Gets the sender.
	 *
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * Sets the sender.
	 *
	 * @param sender the new sender
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * Gets the recipient.
	 *
	 * @return the recipient
	 */
	public String getRecipient() {
		return recipient;
	}

	/**
	 * Sets the recipient.
	 *
	 * @param recipient the new recipient
	 */
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	/**
	 * Gets the email date.
	 *
	 * @return the email date
	 */
	public Timestamp getEmailDate() {
		return emailDate;
	}

	/**
	 * Sets the email date.
	 *
	 * @param emailDate the new email date
	 */
	public void setEmailDate(Timestamp emailDate) {
		this.emailDate = emailDate;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	/**
	 * Checks if is sender.
	 *
	 * @return true, if is sender
	 */
	public boolean isSender() {
		return isSender;
	}

	/**
	 * Sets the sender.
	 *
	 * @param isSender the new sender
	 */
	public void setSender(boolean isSender) {
		this.isSender = isSender;
	}

	/**
	 * Checks if is recipient.
	 *
	 * @return true, if is recipient
	 */
	public boolean isRecipient() {
		return isRecipient;
	}

	/**
	 * Sets the recipient.
	 *
	 * @param isRecipient the new recipient
	 */
	public void setRecipient(boolean isRecipient) {
		this.isRecipient = isRecipient;
	}
	
	/**
	 * Gets the original file name.
	 *
	 * @return the original file name
	 */
	public String getOriginalFileName() {
		return originalFileName;
	}

	/**
	 * Sets the original file name.
	 *
	 * @param originalFileName the new original file name
	 */
	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	/**
	 * Gets the folder name.
	 *
	 * @return the folder name
	 */
	public String getFolderName(){
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(getEmailDate().getTime());
		return String.format("%s_%s_%s_%s", instance.get(Calendar.YEAR), instance.get(Calendar.MONTH)+1, instance.get(Calendar.DATE), getIdMessageAttach());
	}

}
