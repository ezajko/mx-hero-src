package org.mxhero.engine.commons.mail.business;

import java.util.Map;

import org.mxhero.engine.commons.mail.command.Result;

/**
 * Represents data of the mail so it can be used inside rules.
 * @author mmarmol
 */
public class Mail {
	
	private String id;
		
	private String state = MailState.DELIVER;
	
	private String statusReason;
	
	protected InitialData initialData;
	
	protected Headers headers;
	
	protected Subject subject;
	
	protected Recipients recipients;
	
	protected Body body;
	
	protected Attachments attachments;
	
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

	public boolean drop(String reason){
		return false ;
	}

	public InitialData getInitialData() {
		return initialData;
	}

	public void setInitialData(InitialData initialData) {
		this.initialData = initialData;
	}

	public Headers getHeaders() {
		return headers;
	}

	public void setHeaders(Headers headers) {
		this.headers = headers;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Recipients getRecipients() {
		return recipients;
	}

	public void setRecipients(Recipients recipients) {
		this.recipients = recipients;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public Attachments getAttachments() {
		return attachments;
	}

	public void setAttachments(Attachments attachments) {
		this.attachments = attachments;
	}
	
}
