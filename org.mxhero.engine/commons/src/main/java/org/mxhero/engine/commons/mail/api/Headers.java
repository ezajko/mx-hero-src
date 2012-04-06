package org.mxhero.engine.commons.mail.api;

import java.util.Collection;

/**
 * Represents the headers of an email so it can be used in rules.
 * @author mmarmol
 */
public interface Headers {

	/**
	 * @return
	 */
	public Collection<String> getAllHeaderLines();
	
	/**
	 * @param header
	 * @param value
	 */
	public boolean addHeader(String header, String value);

	/**
	 * @param headerLine
	 * @return
	 */
	public boolean addHeaderLine(String headerLine);
	
	/**
	 * @param header
	 * @param Value
	 */
	public boolean setHeaderValue(String header, String value);
	
	/**
	 * @param header
	 * @return
	 */
	public String getHeaderValue(String header);

	/**
	 * @param header
	 * @return
	 */
	public Collection<String> getHeaderValues(String header);
	
	/**
	 * @param header
	 */
	public boolean removeHeader(String header);
	
	/**
	 * @param header
	 * @return
	 */
	public Boolean hasHeader(String header);
	
}
