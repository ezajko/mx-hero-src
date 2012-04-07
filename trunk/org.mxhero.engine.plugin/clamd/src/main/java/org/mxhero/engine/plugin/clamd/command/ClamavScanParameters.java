package org.mxhero.engine.plugin.clamd.command;

import org.mxhero.engine.commons.mail.command.NamedParameters;

/**
 * @author mmarmol
 *
 */
public class ClamavScanParameters extends NamedParameters{

	public static final String REMOVE_INFECTED = "removeInfected";
	public static final String ADD_HEADER = "addHeader";
	public static final String HEADER_NAME = "headerName";

	/**
	 * 
	 */
	public ClamavScanParameters(){
	}
	
	/**
	 * @param parameters
	 */
	public ClamavScanParameters(NamedParameters parameters){
		this.nameToInstance.putAll(parameters.getNameToInstance());
	}
	
	/**
	 * @return
	 */
	public Boolean getRemoveInfected() {
		return get(REMOVE_INFECTED);
	}

	/**
	 * @param removeInfected
	 */
	public void setRemoveInfected(Boolean removeInfected) {
		put(REMOVE_INFECTED, removeInfected);
	}

	/**
	 * @return
	 */
	public Boolean getAddHeader() {
		return get(ADD_HEADER);
	}

	/**
	 * @param addHeader
	 */
	public void setAddHeader(Boolean addHeader) {
		put(ADD_HEADER, addHeader);
	}

	/**
	 * @return
	 */
	public String getHeaderName() {
		return get(HEADER_NAME);
	}

	/**
	 * @param headerName
	 */
	public void setHeaderName(String headerName) {
		put(HEADER_NAME, headerName);
	}

}
