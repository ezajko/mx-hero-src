package org.mxhero.engine.core.internal.mail;

import javax.mail.MessagingException;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.business.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Override the original Subject to implement MimeMessage related logic.
 * @author mmarmol
 */
public class SubjectVO extends Subject{

	private static Logger log = LoggerFactory.getLogger(SubjectVO.class);
	
	private MimeMail mimeMail;
	
	/**
	 * @param mimeMail
	 */
	public SubjectVO(MimeMail mimeMail) {
		super();
		this.mimeMail = mimeMail;
	}


	/**
	 * @see org.mxhero.engine.domain.mail.business.Subject#getSubject()
	 */
	@Override
	public String getSubject() {
		try {
			return this.mimeMail.getMessage().getSubject();
		} catch (MessagingException e) {
			log.error(MailVO.MIME_ERROR,e);
			return "";
		}
	}


	/**
	 * allowed
	 * @see org.mxhero.engine.domain.mail.business.Subject#setSubject(java.lang.String)
	 */
	@Override
	public void setSubject(String subject) {
		try {
			this.mimeMail.getMessage().setSubject(subject);
		} catch (MessagingException e) {
			log.error(MailVO.MIME_ERROR,e);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SubjectVO [getSubject()=").append(getSubject()).append(
				"]");
		return builder.toString();
	}

}
