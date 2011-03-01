package org.mxhero.engine.core.mail;

import java.util.Map;

import javax.mail.MessagingException;

import org.mxhero.engine.core.mail.command.CommandResolver;
import org.mxhero.engine.domain.mail.business.Mail;
import org.mxhero.engine.domain.mail.command.Result;
import org.mxhero.engine.domain.mail.MimeMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Override the original Mail to implement MimeMessage related logic.
 * @author mmarmol
 */
public final class MailVO extends Mail {

	public static final String MIME_ERROR = "MimeMessage error for mail";

	public static final String CHANGE_ERROR = "Can't be changed for mail";

	private static Logger log = LoggerFactory.getLogger(MailVO.class);

	private MimeMail mimeMail;
	
	private static CommandResolver finder;

	/**
	 * @param mimeMail
	 */
	public MailVO(MimeMail mimeMail) {
		this.mimeMail = mimeMail;
	}

	/**
	 * @see org.mxhero.engine.domain.mail.business.Mail#getId()
	 */
	@Override
	public String getId() {
		try {
			return this.mimeMail.getMessage().getMessageID();
		} catch (MessagingException e) {
			log.error(MIME_ERROR + this, e);
			return "";
		}
	}

	/**
	 * Should not be call, just override it for drools.
	 * @see org.mxhero.engine.domain.mail.business.Mail#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		log.warn(CHANGE_ERROR + this);
	}

	/**
	 * @param commandServiceId
	 * @return
	 */
	@Override
	public Result cmd(String commandServiceId, String... params) {
		if (MailVO.finder == null) {
			log.warn("finder is not set");
			return null;
		}
		return MailVO.finder.resolve(this.mimeMail, commandServiceId, params);
	}

	/**
	 * Sets the static finder to resolve commands
	 * @param finder
	 */
	public static void setFinder(CommandResolver finder) {
		MailVO.finder = finder;
	}

	/**
	 * @return
	 */
	@Override
	public String getState() {
		return this.mimeMail.getStatus();
	}

	/**
	 * allowed
	 * @param state
	 */
	@Override
	public void setState(String state) {
		this.mimeMail.setStatus(state);
	}

	/**
	 * @return the statusReason
	 */
	@Override
	public String getStatusReason() {
		return this.mimeMail.getStatusReason();
	}

	/**
	 * allowed
	 * @param statusReason the statusReason to set
	 */
	@Override
	public void setStatusReason(String statusReason) {
		this.mimeMail.setStatusReason(statusReason);
	}

	
	@Override
	public Map<String,String> getProperties(){
		return mimeMail.getProperties();
	}
	
	@Override
	public void setProperties(Map<String,String> properties) {
		log.warn(CHANGE_ERROR + this);
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MailVO [getId()=").append(getId()).append(
				", getState()=").append(getState()).append(
				", getStatusReason()=").append(getStatusReason()).append("]");
		return builder.toString();
	}

}
