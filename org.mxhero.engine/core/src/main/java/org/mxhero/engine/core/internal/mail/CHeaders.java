package org.mxhero.engine.core.internal.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;

import javax.mail.MessagingException;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.api.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CHeaders implements Headers{

	private static Logger log = LoggerFactory.getLogger(CHeaders.class);
	
	private MimeMail mimeMail;

	public CHeaders(MimeMail mimeMail) {
		this.mimeMail = mimeMail;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<String> getAllHeaderLines() {
		try {
			return Collections.list((Enumeration<String>)this.mimeMail.getMessage().getAllHeaders());
		} catch (MessagingException e) {
			log.warn(e.getMessage());
			return new ArrayList<String>();
		}
	}

	@Override
	public boolean addHeader(String header, String value) {
		try {
			this.mimeMail.getMessage().addHeader(header, value);
			return true;
		} catch (MessagingException e) {
			log.warn(e.getMessage());
			return false;
		}
	}

	@Override
	public boolean addHeaderLine(String headerLine) {
		try {
			this.mimeMail.getMessage().addHeaderLine(headerLine);
			return true;
		} catch (MessagingException e) {
			log.error(e.getMessage());
			return false;
		}
	}

	@Override
	public boolean setHeaderValue(String header, String value) {
		try {
			this.mimeMail.getMessage().setHeader(header, value);
			return true;
		} catch (MessagingException e) {
			log.warn(e.getMessage());
			return false;
		}
	}

	@Override
	public String getHeaderValue(String header) {
		try {
			String internetHeader = this.mimeMail.getMessage().getHeader(header,null);
			return internetHeader;
		} catch (MessagingException e) {
			log.warn(e.getMessage());
		}
		return null;
	}

	@Override
	public Collection<String> getHeaderValues(String header) {
		try {
			String[] values = mimeMail.getMessage().getHeader(header);
			if(values!=null){
				return Arrays.asList(values);
			}
		} catch (MessagingException e) {
			log.warn(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean removeHeader(String header) {
		try {
			this.mimeMail.getMessage().removeHeader(header);
			return true;
		} catch (MessagingException e) {
			log.warn(e.getMessage());
			return false;
		}
	}

	@Override
	public Boolean hasHeader(String header) {
		String[] headers = null;
		try {
			headers = this.mimeMail.getMessage().getHeader(header);
		} catch (MessagingException e) {
			log.warn(e.getMessage());
		}
		if(headers!=null && headers.length>0){
			return true;
		}
		return false;
	}

}
