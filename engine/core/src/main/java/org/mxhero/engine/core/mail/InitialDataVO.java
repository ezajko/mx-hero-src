package org.mxhero.engine.core.mail;

import java.util.Date;

import javax.mail.MessagingException;

import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.InitialData;
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
	public InitialDataVO(MimeMail mimemail){
		this.mimeMail = mimemail;
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
	 * @see org.mxhero.engine.domain.mail.business.InitialData#getInitialSender()
	 */
	@Override
	public String getInitialSender() {
		return this.mimeMail.getInitialSender();
	}

	/**
	 * Should not be call, just override it for drools.
	 * @see org.mxhero.engine.domain.mail.business.InitialData#setInitialSender(java.lang.String)
	 */
	@Override
	public void setInitialSender(String initialSender) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}
	
	/**
	 * @see org.mxhero.engine.domain.mail.business.InitialData#getInitialRecipient()
	 */
	@Override
	public String getInitialRecipient() {
		return this.mimeMail.getRecipient();
	}

	/**
	 * Should not be call, just override it for drools.
	 * @see org.mxhero.engine.domain.mail.business.InitialData#setInitialRecipient(java.lang.String)
	 */
	@Override
	public void setInitialRecipient(String initialRecipient) {
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
			log.error(MailVO.MIME_ERROR + this, e);
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
			log.error(MailVO.MIME_ERROR + this, e);
		}
	}

	/**
	 * @see org.mxhero.engine.domain.mail.business.InitialData#getReceivedDate()
	 */
	@Override
	public Date getReceivedDate() {
		try {
			return this.mimeMail.getMessage().getReceivedDate();
		} catch (MessagingException e) {
			log.error(MailVO.MIME_ERROR + this, e);
			return null;
		}
	}

	/**
	 * Should not be call, just override it for drools.
	 * @see org.mxhero.engine.domain.mail.business.InitialData#setReceivedDate(java.util.Date)
	 */
	@Override
	public void setReceivedDate(Date receivedDate) {
		log.warn(MailVO.CHANGE_ERROR + this);
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InitialDataVO [getPhase()=").append(getPhase()).append(
				", getInitialSender()=").append(getInitialSender()).append(
				", getInitialRecipient()=").append(getInitialRecipient())
				.append(", getInitialSize()=").append(getInitialSize()).append(
						", getSentDate()=").append(getSentDate()).append(
						", getReceivedDate()=").append(getReceivedDate())
				.append("]");
		return builder.toString();
	}

}
