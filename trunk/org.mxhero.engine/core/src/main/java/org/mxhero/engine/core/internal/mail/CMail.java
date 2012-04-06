package org.mxhero.engine.core.internal.mail;

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
	public int getInitialSize() {
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
}
