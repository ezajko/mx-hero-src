package org.mxhero.engine.core.internal.mail;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.mxhero.engine.commons.domain.User;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.api.Attachments;
import org.mxhero.engine.commons.mail.api.Body;
import org.mxhero.engine.commons.mail.api.Headers;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.mail.api.Recipients;
import org.mxhero.engine.commons.mail.command.NamedParameters;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.core.internal.mail.command.CommandResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mmarmol
 *
 */
public class CMail implements Mail{

	private static Logger log = LoggerFactory.getLogger(CMail.class);
	private static CommandResolver resolver;
	
	private MimeMail mimeMail;
	private User sender;
	private User recipient;
	private User fromSender;
	private List<User> recipientsInHeaders;
	private Attachments attachments;
	private Body body;
	private Headers headers;
	private Recipients recipients;
	
	/**
	 * @param mimeMail
	 * @param sender
	 * @param recipient
	 * @param fromSender
	 * @param recipientsInHeaders
	 */
	public CMail(MimeMail mimeMail, User sender, User recipient,
			User fromSender, List<User> recipientsInHeaders) {
		this.mimeMail = mimeMail;
		this.sender = sender;
		this.recipient = recipient;
		this.fromSender = fromSender;
		this.recipientsInHeaders = recipientsInHeaders;
		this.attachments = new CAttachments(mimeMail);
		this.body = new CBody(mimeMail);
		this.headers = new CHeaders(mimeMail);
		this.recipients = new CRecipients(mimeMail);
	}

	@Override
	public String getId() {
		return this.mimeMail.getMessageId();
	}

	@Override
	public Result cmd(String commandServiceId, NamedParameters params) {
		if (CMail.resolver == null) {
			log.warn("finder is not set");
			return null;
		}
		return CMail.resolver.resolve(this.mimeMail, commandServiceId, params);
	}

	@Override
	public Phase getPhase() {
		return mimeMail.getPhase();
	}

	@Override
	public Status getStatus() {
		return mimeMail.getStatus();
	}

	@Override
	public Flow getFlow() {
		return mimeMail.getFlow();
	}

	@Override
	public String getStatusReason() {
		return mimeMail.getStatusReason();
	}

	@Override
	public long getInitialSize() {
		return mimeMail.getInitialSize();
	}

	@Override
	public User getSender() {
		return sender;
	}

	@Override
	public User getFromSender() {
		return fromSender;
	}

	@Override
	public User getRecipient() {
		return recipient;
	}

	@Override
	public List<User> getRecipientsInHeaders() {
		return recipientsInHeaders;
	}

	@Override
	public Map<String, String> getProperties() {
		return mimeMail.getProperties();
	}

	@Override
	public boolean drop(String reason) {
		synchronized (this.mimeMail) {
			if(mimeMail.equals(Mail.Status.drop)){
				return false;
			}else{
				mimeMail.setStatus(Mail.Status.drop);
				mimeMail.setStatusReason(reason);
				return true;
			}
		}
	}

	@Override
	public boolean redirect(String reason) {
		synchronized (this.mimeMail) {
			if(mimeMail.equals(Mail.Status.redirect)){
				return false;
			}else{
				mimeMail.setStatus(Mail.Status.redirect);
				mimeMail.setStatusReason(reason);
				return true;
			}
		}
	}

	@Override
	public Headers getHeaders() {
		return headers;
	}

	@Override
	public String getSubject() {
		try {
			return mimeMail.getMessage().getSubject();
		} catch (MessagingException e){log.warn(e.getMessage());}
		return "";
	}

	@Override
	public void setSubject(String subject) {
		try {
			mimeMail.getMessage().setSubject(subject);
		} catch (MessagingException e){log.warn(e.getMessage());}
	}

	@Override
	public Recipients getRecipients() {
		return recipients;
	}

	@Override
	public Body getBody() {
		return body;
	}

	@Override
	public Attachments getAttachments() {
		return attachments;
	}

	public static void setResolver(CommandResolver resolver){
		CMail.resolver=resolver;
	}

	@Override
	public long getSize() {
		long size;

		try {
			size = this.mimeMail.getMessage().getSize();
		} catch (MessagingException e1) {
			log.warn("error while trying to read message size");
			size = -1;
		}
		if (size != -1) {
			@SuppressWarnings("rawtypes")
			Enumeration e;
			try {
				e = this.mimeMail.getMessage().getAllHeaderLines();
				if (e.hasMoreElements()) {
					size += 2;
				}
				while (e.hasMoreElements()) {
					// add 2 bytes for the CRLF
					size += ((String) e.nextElement()).length() + 2;
				}
			} catch (MessagingException e1) {
				log.warn("error while trying to read message headers");
				size = -1;
			}
		}

		if (size == -1) {
			SizeCalculatorOutputStream out = new SizeCalculatorOutputStream();
			try {
				this.mimeMail.getMessage().writeTo(out);
			} catch (IOException e) {
				log.warn("error while trying to read stream", e);
				return -1;
			} catch (MessagingException e) {
				log.warn("error while trying to read stream", e);
				return -1;
			}
			size = out.getSize();
		}
		return size;
	}
	
	/**
	 * Slow method to calculate the exact size of a message!
	 * 
	 * @author mmarmol
	 */
	private static final class SizeCalculatorOutputStream extends OutputStream {
		private long size = 0;

		/**
		 * @see java.io.OutputStream#write(int)
		 */
		public void write(int arg0) throws IOException {
			size++;
		}

		/**
		 * @return return the amount of bytes written.
		 */
		public long getSize() {
			return size;
		}

		/**
		 * @see java.io.OutputStream#write(byte[], int, int)
		 */
		public void write(byte[] arg0, int arg1, int arg2) throws IOException {
			size += arg2;
		}

		/**
		 * @see java.io.OutputStream#write(byte[])
		 */
		public void write(byte[] arg0) throws IOException {
			size += arg0.length;
		}
	}
}
