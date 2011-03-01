package org.mxhero.engine.domain.mail.business;

import java.util.Map;

import org.mxhero.engine.domain.mail.command.Result;

/**
 * Represents data of the mail so it can be used inside rules.
 * @author mmarmol
 */
public class Mail {
	
	private String id;
		
	private String state = MailState.DELIVER;
	
	private String statusReason;
	
	private Map<String, String> properties;
	
	/**
	 * 
	 */
	public Mail() {
	}

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @param commandServiceId
	 * @return
	 */
	public Result cmd(String commandServiceId, String... params){
		return  new Result();
	}
	
	/**
	 * @return
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the statusReason
	 */
	public String getStatusReason() {
		return statusReason;
	}

	/**
	 * @param statusReason the statusReason to set
	 */
	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

}
