package org.mxhero.engine.commons.mail.business;

import java.util.Collection;

/**
 * Represents the headers of an email so it can be used in rules.
 * @author mmarmol
 */
public class Headers {

	private Collection<String> allHeaders;
	
	private String allHeadersStr;

	/**
	 * @return the allHeaders
	 */
	public Collection<String> getAllHeaders() {
		return allHeaders;
	}

	/**
	 * @param allHeaders the allHeaders to set
	 */
	public void setAllHeaders(Collection<String> allHeaders) {
		this.allHeaders = allHeaders;
	}

	/**
	 * @return the allHeadersStr
	 */
	public String getAllHeadersStr() {
		return allHeadersStr;
	}

	/**
	 * @param allHeadersStr the allHeadersStr to set
	 */
	public void setAllHeadersStr(String allHeadersStr) {
		this.allHeadersStr = allHeadersStr;
	}

	/**
	 * @param header
	 * @param value
	 */
	public boolean addHeader(String header, String value){
		return false;
	}

	public boolean addHeaderLine(String headerLine){
		return false;
	}
	
	/**
	 * @param header
	 * @param Value
	 */
	public boolean setHeaderValue(String header, String Value){ 
		return false;
	}
	
	/**
	 * @param header
	 * @return
	 */
	public String getHeaderValue(String header){
		return null;
	}
	
	/**
	 * @param header
	 */
	public boolean removeHeader(String header){
		return false;
	}
	
	/**
	 * @param header
	 * @return
	 */
	public Boolean hasHeader(String header){
		return false;
	}
}
