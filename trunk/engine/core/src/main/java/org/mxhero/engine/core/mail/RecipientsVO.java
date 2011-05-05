package org.mxhero.engine.core.mail;

import java.util.Arrays;
import java.util.Collection;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage.RecipientType;

import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.Recipients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Override the original Recipients to implement MimeMessage related logic.
 * @author mmarmol
 */
public class RecipientsVO extends Recipients{

	private static Logger log = LoggerFactory.getLogger(RecipientsVO.class);
	
	private MimeMail mimeMail;
	
	/**
	 * @param mimeMail
	 */
	public RecipientsVO(MimeMail mimeMail){
		this.mimeMail = mimeMail;
	}

	/**
	 * @see org.mxhero.engine.domain.mail.business.Recipients#getAllRecipients()
	 */
	@Override
	public Collection<String> getAllRecipients() {
		try {
			return MailUtils.toStringCollection(this.mimeMail.getMessage().getAllRecipients());
		} catch (MessagingException e) {
			log.error(MailVO.MIME_ERROR,e.getMessage()+"\n"+getFromHeaders());
			return MailUtils.toStringCollection(null);
		}
	}
	
	/**
	 * Should not be call, just override it for drools.
	 * @see org.mxhero.engine.domain.mail.business.Recipients#setAllRecipients(java.util.Collection)
	 */
	@Override
	public void setAllRecipients(Collection<String> allRecipients) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	/**
	 * @see org.mxhero.engine.domain.mail.business.Recipients#getToRecipients()
	 */
	@Override
	public Collection<String> getToRecipients() {
		return getRecipientsByType(RecipientType.TO);
	}
	
	/**
	 * Should not be call, just override it for drools.
	 * @see org.mxhero.engine.domain.mail.business.Recipients#setToRecipients(java.util.Collection)
	 */
	@Override
	public void setToRecipients(Collection<String> toRecipients) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	/**
	 * @see org.mxhero.engine.domain.mail.business.Recipients#getBccRecipients()
	 */
	@Override
	public Collection<String> getBccRecipients() {
		return getRecipientsByType(RecipientType.BCC);
	}

	/**
	 * Should not be call, just override it for drools.
	 * @see org.mxhero.engine.domain.mail.business.Recipients#setBccRecipients(java.util.Collection)
	 */
	@Override
	public void setBccRecipients(Collection<String> bccRecipients) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	/**
	 * @see org.mxhero.engine.domain.mail.business.Recipients#getCcRecipients()
	 */
	@Override
	public Collection<String> getCcRecipients() {
		return getRecipientsByType(RecipientType.CC);
	}

	/**
	 * Should not be call, just override it for drools.
	 * @see org.mxhero.engine.domain.mail.business.Recipients#setCcRecipients(java.util.Collection)
	 */
	@Override
	public void setCcRecipients(Collection<String> ccRecipients) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	/**
	 * @see org.mxhero.engine.domain.mail.business.Recipients#getNgRecipients()
	 */
	@Override
	public Collection<String> getNgRecipients() {
		return getRecipientsByType(RecipientType.NEWSGROUPS);
	}

	/**
	 * Should not be call, just override it for drools.
	 * @see org.mxhero.engine.domain.mail.business.Recipients#setNgRecipients(java.util.Collection)
	 */
	@Override
	public void setNgRecipients(Collection<String> ngRecipients) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	/**
	 * @see org.mxhero.engine.domain.mail.business.Recipients#getAllRecipientsStr()
	 */
	@Override
	public String getAllRecipientsStr() {
			return Arrays.toString(this.getAllRecipients().toArray()).replace("[","").replace("]", "");
	}

	/**
	 * Should not be call, just override it for drools.
	 * @see org.mxhero.engine.domain.mail.business.Recipients#setAllRecipientsStr(java.lang.String)
	 */
	@Override
	public void setAllRecipientsStr(String allRecipientsStr) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	/**
	 * @see org.mxhero.engine.domain.mail.business.Recipients#getToRecipientsStr()
	 */
	@Override
	public String getToRecipientsStr() {
		return getRecipientsByTypeString(RecipientType.TO);
	}

	/**
	 * Should not be call, just override it for drools.
	 * @see org.mxhero.engine.domain.mail.business.Recipients#setToRecipientsStr(java.lang.String)
	 */
	@Override
	public void setToRecipientsStr(String toRecipientsStr) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	/**
	 * @see org.mxhero.engine.domain.mail.business.Recipients#getBccRecipientsStr()
	 */
	@Override
	public String getBccRecipientsStr() {
		return getRecipientsByTypeString(RecipientType.BCC);
	}

	/**
	 * Should not be call, just override it for drools.
	 * @see org.mxhero.engine.domain.mail.business.Recipients#setBccRecipientsStr(java.lang.String)
	 */
	@Override
	public void setBccRecipientsStr(String bccRecipientsStr) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	/**
	 * @see org.mxhero.engine.domain.mail.business.Recipients#getCcRecipientsStr()
	 */
	@Override
	public String getCcRecipientsStr() {
		return getRecipientsByTypeString(RecipientType.CC);
	}

	/**
	 * Should not be call, just override it for drools.
	 * @see org.mxhero.engine.domain.mail.business.Recipients#setCcRecipientsStr(java.lang.String)
	 */
	@Override
	public void setCcRecipientsStr(String ccRecipientsStr) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	/**
	 * @see org.mxhero.engine.domain.mail.business.Recipients#getNgRecipientsStr()
	 */
	@Override
	public String getNgRecipientsStr() {
		return getRecipientsByTypeString(RecipientType.NEWSGROUPS);
	}

	/**
	 * Should not be call, just override it for drools.
	 * @see org.mxhero.engine.domain.mail.business.Recipients#setNgRecipientsStr(java.lang.String)
	 */
	@Override
	public void setNgRecipientsStr(String ngRecipientsStr) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	/**
	 * @param type
	 * @return
	 */
	private Collection<String> getRecipientsByType(javax.mail.Message.RecipientType type){
		try {
			return MailUtils.toStringCollection(this.mimeMail.getMessage().getRecipients(type));
		} catch (MessagingException e) {
			log.error(MailVO.MIME_ERROR,e.getMessage()+"\n"+getFromHeaders());
			return MailUtils.toStringCollection(null);
		}	
	}
		
	/**
	 * @param type
	 * @return
	 */
	private String getRecipientsByTypeString(javax.mail.Message.RecipientType type){
			return Arrays.toString(getRecipientsByType(type).toArray()).replace("[","").replace("]", "");
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RecipientsVO [getAllRecipientsStr()=").append(
				getAllRecipientsStr()).append(", getBccRecipientsStr()=")
				.append(getBccRecipientsStr())
				.append(", getCcRecipientsStr()=").append(getCcRecipientsStr())
				.append(", getNgRecipientsStr()=").append(getNgRecipientsStr())
				.append(", getToRecipientsStr()=").append(getToRecipientsStr())
				.append("]");
		return builder.toString();
	}

	private String getFromHeaders(){
		String address = "";
		String[] headerValue = null;
		
		try {
			headerValue = mimeMail.getMessage().getHeader("To");
			if(headerValue!=null){
				address=address+"To:"+Arrays.toString(headerValue)+"\n";
			}
		} catch (MessagingException e) {
			log.error("To:",e);
		}
		try {
			headerValue = mimeMail.getMessage().getHeader("Cc");
			if(headerValue!=null){
				address=address+"Cc:"+Arrays.toString(headerValue)+"\n";
			}
		} catch (MessagingException e) {
			log.error("Cc:",e);
		}
		try {
			headerValue = mimeMail.getMessage().getHeader("Bcc");
			if(headerValue!=null){
				address=address+"Bcc:"+Arrays.toString(headerValue)+"\n";
			}
		} catch (MessagingException e) {
			log.error("Bcc:",e);
		}
		
		return address;
	}
	
}
