package org.mxhero.engine.core.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;

import javax.mail.MessagingException;

import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Override the original Headers to implement MimeMessage related logic.
 * @author mmarmol
 */
public class HeadersVO extends Headers{

	private static Logger log = LoggerFactory.getLogger(HeadersVO.class);
	
	private MimeMail mimeMail;
	
	/**
	 * @param mimeMail
	 */
	public HeadersVO(MimeMail mimeMail){
		this.mimeMail = mimeMail;
	}
	
	/**
	 * @see org.mxhero.engine.domain.mail.business.Headers#getAllHeaders()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<String> getAllHeaders() {
		try {
			return Collections.list((Enumeration<String>)this.mimeMail.getMessage().getAllHeaderLines());
		} catch (MessagingException e) {
			log.error(MailVO.MIME_ERROR+this,e);
			return new ArrayList<String>();
		}
	}
	
	/**
	 * Should not be call, just override it for drools.
	 * @see org.mxhero.engine.domain.mail.business.Headers#setAllHeaders(java.util.Collection)
	 */
	@Override
	public void setAllHeaders(Collection<String> allHeaders) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}
	
	/**
	 * @see org.mxhero.engine.domain.mail.business.Headers#getAllHeadersStr()
	 */
	@Override
	public String getAllHeadersStr() {
		return Arrays.toString(getAllHeaders().toArray());
	}
	
	/**
	 * Should not be call, just override it for drools.
	 * @see org.mxhero.engine.domain.mail.business.Headers#setAllHeadersStr(java.lang.String)
	 */
	@Override
	public void setAllHeadersStr(String allHeadersStr) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HeadersVO [getAllHeadersStr()=").append(
				getAllHeadersStr()).append("]");
		return builder.toString();
	}

}
