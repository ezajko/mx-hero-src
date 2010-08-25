package org.mxhero.engine.core.mail;

import java.util.Arrays;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Override the original Sender to implement MimeMessage related logic.
 * @author mmarmol
 */
public class SenderVO extends Sender{

	private static Logger log = LoggerFactory.getLogger(SenderVO.class);
	
	private MimeMail mimeMail;
	
	/**
	 * @param mimeMail
	 */
	public SenderVO(MimeMail mimeMail){
		this.mimeMail = mimeMail;
	}
	
	/**
	 * @see org.mxhero.engine.domain.mail.business.Sender#getSender()
	 */
	@Override
	public String getSender() {
		try {
			Address senderAddres = this.mimeMail.getMessage().getSender();
			if(senderAddres==null){
				return "";
			}
			return senderAddres.toString();
		} catch (MessagingException e) {
			log.error(MailVO.MIME_ERROR+this,e);
			return "";
		}
	}

	/**
	 * allowed
	 * @see org.mxhero.engine.domain.mail.business.Sender#setSender(java.lang.String)
	 */
	@Override
	public void setSender(String sender) {
		try {
			this.mimeMail.getMessage().setSender(new InternetAddress(sender));
		} catch (AddressException e) {
			log.error("Error in address for mail "+this,e);
		} catch (MessagingException e) {
			log.error(MailVO.MIME_ERROR+this,e);
		}
	}

	/**
	 * @see org.mxhero.engine.domain.mail.business.Sender#getFrom()
	 */
	@Override
	public String getFrom() {
		try {
			return Arrays.toString(this.mimeMail.getMessage().getFrom());
		} catch (MessagingException e) {
			log.error(MailVO.MIME_ERROR+this,e);
			return "";
		}
	}

	/**
	 * allowed
	 * @see org.mxhero.engine.domain.mail.business.Sender#setFrom(java.lang.String)
	 */
	@Override
	public void setFrom(String from) {
		try {
			this.mimeMail.getMessage().setFrom(new InternetAddress(from));
		} catch (AddressException e) {
			log.error("Error in address for mail "+this,e);
		} catch (MessagingException e) {
			log.error(MailVO.MIME_ERROR+this,e);
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SenderVO [getFrom()=").append(getFrom()).append(
				", getSender()=").append(getSender()).append("]");
		return builder.toString();
	}


}
