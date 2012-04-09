package org.mxhero.engine.plugin.spamd.command;

import org.mxhero.engine.commons.mail.command.NamedParameters;

/**
 * @author mmarmol
 *
 */
public class SpamScanParameters extends NamedParameters {

	public static final String PREFIX = "prefix";
	public static final String ADD_HEADERS = "addHeaders";
	public static final String FLAG_HEADER = "flagHeader";
	public static final String STATUS_HEADER = "statusHeader";

	/**
	 * 
	 */
	public SpamScanParameters(){
	}
	
	/**
	 * @param prefix
	 * @param addHeaders
	 */
	public SpamScanParameters(String prefix, Boolean addHeaders){
		this.setPrefix(prefix);
		this.setAddHeaders(addHeaders);
	}
	
	/**
	 * @param parameters
	 */
	public SpamScanParameters(NamedParameters parameters){
		this.nameToInstance.putAll(parameters.getNameToInstance());
	}
	
	/**
	 * @return
	 */
	public String getPrefix() {
		return get(PREFIX);
	}

	/**
	 * @param prefix
	 */
	public void setPrefix(String prefix) {
		put(PREFIX,prefix);
	}

	/**
	 * @return
	 */
	public Boolean getAddHeaders() {
		return get(ADD_HEADERS);
	}

	/**
	 * @param addHeaders
	 */
	public void setAddHeaders(Boolean addHeaders) {
		put(ADD_HEADERS,addHeaders);
	}

	/**
	 * @return
	 */
	public String getFlagHeader() {
		return get(FLAG_HEADER);
	}

	/**
	 * @param flagHeader
	 */
	public void setFlagHeader(String flagHeader) {
		put(FLAG_HEADER,flagHeader);
	}

	/**
	 * @return
	 */
	public String getStatusHeader() {
		return get(STATUS_HEADER);
	}

	/**
	 * @param statusHeader
	 */
	public void setStatusHeader(String statusHeader) {
		put(STATUS_HEADER,statusHeader);
	}

}
