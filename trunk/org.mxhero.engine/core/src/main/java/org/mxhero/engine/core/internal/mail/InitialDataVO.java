package org.mxhero.engine.core.internal.mail;

import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.business.InitialData;
import org.mxhero.engine.commons.mail.business.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Override the original InitialData to implement MimeMessage related logic.
 * @author mmarmol
 */
public class InitialDataVO extends InitialData{

	private static Logger log = LoggerFactory.getLogger(InitialDataVO.class);
	
	private MimeMail mimeMail;
	
	/**
	 * @param mimemail
	 */
	public InitialDataVO(MimeMail mimemail, User sender, User recipient, User from, List<User> recipientsInHeaders){
		this.mimeMail = mimemail;
		super.setSender(sender);
		super.setRecipient(recipient);
		super.setFromSender(from);
		super.setRecipientsInHeaders(recipientsInHeaders);
	}
	
	/**
	 * @see org.mxhero.engine.domain.mail.business.InitialData#getInitialSize()
	 */
	@Override
	public int getInitialSize() {
		return this.mimeMail.getInitialSize();
	}

	/**
	 * Should not be call, just override it for drools.
	 * @see org.mxhero.engine.domain.mail.business.InitialData#setInitialSize(int)
	 */
	@Override
	public void setInitialSize(int initialSize) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	/**
	 * @param sender
	 */
	@Override
	public void setSender(User sender) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}
	
	/**
	 * @param from
	 */
	@Override
	public void setFromSender(User from) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	/**
	 * @param recipient
	 */
	@Override
	public void setRecipient(User recipient) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}
	
	/**
	 * @see org.mxhero.engine.domain.mail.business.InitialData#getSentDate()
	 */
	@Override
	public Date getSentDate() {
		try {
			return this.mimeMail.getMessage().getSentDate();
		} catch (MessagingException e) {
			log.error(MailVO.MIME_ERROR, e);
			return null;
		}
	}

	/**
	 * Can be set
	 * @see org.mxhero.engine.domain.mail.business.InitialData#setSentDate(java.util.Date)
	 */
	@Override
	public void setSentDate(Date sentDate) {
		try {
			this.mimeMail.getMessage().setSentDate(sentDate);
		} catch (MessagingException e) {
			log.error(MailVO.MIME_ERROR, e);
		}
	}
	
	/**
	 * @see org.mxhero.engine.domain.mail.business.InitialData#getPhase()
	 */
	@Override
	public String getPhase() {
		return this.mimeMail.getPhase();
	}

	/**
	 * Should not be call, just override it for drools.
	 * @see org.mxhero.engine.domain.mail.business.InitialData#setPhase(java.lang.String)
	 */
	@Override
	public void setPhase(String phase) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	/** 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InitialDataVO [getInitialSize()=")
				.append(getInitialSize()).append(", getSentDate()=")
				.append(getSentDate()).append(", getReceivedDate()=")
				.append(getPhase()).append(", getSender()=")
				.append(getSender()).append(", getRecipient()=")
				.append(getRecipient()).append("]");
		return builder.toString();
	}


}
