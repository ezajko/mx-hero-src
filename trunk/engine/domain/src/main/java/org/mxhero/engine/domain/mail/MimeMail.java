package org.mxhero.engine.domain.mail;

import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

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

	private Collection<String> recipients;

	private String responseServiceId;

	private int initialSize;

	private MimeMessage message;

	private String phase = RulePhase.SEND;

	private String status = MailState.DELIVER;

	private String statusReason;

	/**
	 * @param from
	 * @param recipient
	 * @param data
	 * @param responseServiceId
	 * @throws MessagingException
	 */
	public MimeMail(String from, Collection<String> recipient, byte[] data,
			String responseServiceId) throws MessagingException {
		this(from, recipient, responseServiceId);
		this.initialSize = data.length;
		this.message = new MimeMessage(Session
				.getDefaultInstance(new Properties()),
				new ByteArrayInputStream(data));

	}

	/**
	 * @param from
	 * @param recipients
	 * @param data
	 * @param responseServiceId
	 * @throws MessagingException
	 */
	@SuppressWarnings("unchecked")
	public MimeMail(String from, Collection<String> recipients,
			MimeMessage data, String responseServiceId)
			throws MessagingException {
		this(from, recipients, responseServiceId);
		int headerSize = 0;
		Enumeration e = data.getAllHeaderLines();
		if (e.hasMoreElements()) {
			headerSize += 2;
		}
		while (e.hasMoreElements()) {
			// add 2 bytes for the CRLF
			headerSize += ((String) e.nextElement()).length() + 2;
		}
		this.initialSize = data.getSize() + headerSize;
		this.message = new MimeMessage(data);
	}

	/**
	 * @param from
	 * @param recipients
	 * @param responseServiceId
	 */
	private MimeMail(String from, Collection<String> recipients,
			String responseServiceId) {
		this.sequence = Sequencer.getInstance().getNextSequence();
		this.time = new Timestamp(System.currentTimeMillis());
		this.responseServiceId = responseServiceId;
		this.recipients = recipients;
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
	public Collection<String> getRecipients() {
		return this.recipients;
	}

	/**
	 * @param recipients
	 *            the recipients to set
	 */
	public void setRecipients(Collection<String> recipients) {
		this.recipients = recipients;
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
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MimeMail [sequence=").append(sequence)
				.append(", time=").append(time).append(", phase=")
				.append(phase).append(", initialSender=").append(initialSender)
				.append(", recipient=").append(recipient).append(
						", recipients=").append(recipients).append(
						", responseServiceId=").append(responseServiceId)
				.append(", status=").append(status).append(", statusReason=")
				.append(statusReason).append("]");
		return builder.toString();
	}

}
