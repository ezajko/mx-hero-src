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
	 * @param header
	 * @param value
	 */
	public boolean addHeader(String header, String value){
		try {
			this.mimeMail.getMessage().addHeader(header, value);
			return true;
		} catch (MessagingException e) {
			log.error(MailVO.MIME_ERROR+this,e);
			return false;
		}
	}

	/**
	 * @param header
	 */
	public boolean addHeaderLine(String header){
		try {
			this.mimeMail.getMessage().addHeaderLine(header);
			return true;
		} catch (MessagingException e) {
			log.error(MailVO.MIME_ERROR+this,e);
			return false;
		}
	}

	
	/**
	 * @param header
	 * @param Value
	 */
	public boolean setHeaderValue(String header, String value){ 
		try {
			this.mimeMail.getMessage().setHeader(header, value);
			return true;
		} catch (MessagingException e) {
			log.error(MailVO.MIME_ERROR+this,e);
			return false;
		}
	}
	
	/**
	 * @param header
	 * @return
	 */
	public String getHeaderValue(String header){
		try {
			String internetHeader = this.mimeMail.getMessage().getHeader(header,null);
			log.debug("header found:"+internetHeader+" for header:"+header);
			return internetHeader;
		} catch (MessagingException e) {
			log.error(MailVO.MIME_ERROR+this,e);
		}
		return null;
	}
	
	/**
	 * @param header
	 */
	public boolean removeHeader(String header){
		try {
			this.mimeMail.getMessage().removeHeader(header);
			return true;
		} catch (MessagingException e) {
			log.error(MailVO.MIME_ERROR+this,e);
			return false;
		}
	}
	
	/**
	 * @param header
	 * @return
	 */
	public Boolean hasHeader(String header){
		String[] headers = null;
		try {
			headers = this.mimeMail.getMessage().getHeader(header);
		} catch (MessagingException e) {
			log.error(MailVO.MIME_ERROR+this,e);
		}
		if(headers!=null && headers.length>0){
			return true;
		}
		return false;
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
