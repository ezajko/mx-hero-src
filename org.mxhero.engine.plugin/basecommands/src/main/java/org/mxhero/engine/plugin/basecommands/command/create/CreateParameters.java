package org.mxhero.engine.plugin.basecommands.command.create;

import org.mxhero.engine.commons.mail.command.NamedParameters;

/**
 * @author mmarmol
 *
 */
public class CreateParameters extends NamedParameters{

	public static final String SENDER = "sender";
	public static final String RECIPIENTS = "recipients";
	public static final String SUBJECT = "subject";
	public static final String TEXT = "text";
	public static final String OUTPUT_SERVICE = "outputService";

	/**
	 * 
	 */
	public CreateParameters(){
	}
	
	/**
	 * @param sender
	 * @param recipients
	 * @param subject
	 * @param text
	 * @param outputService
	 */
	public CreateParameters(String sender, String recipients, String subject, String text, String outputService){
		this.setSender(sender);
		this.setRecipients(recipients);
		this.setSubject(subject);
		this.setText(text);
		this.setOutputService(outputService);
	}
	
	/**
	 * @param parameters
	 */
	public CreateParameters(NamedParameters parameters){
		this.nameToInstance.putAll(parameters.getNameToInstance());
	}
	
	/**
	 * @return
	 */
	public String getSender() {
		return get(SENDER);
	}

	/**
	 * @param sender
	 */
	public void setSender(String sender) {
		put(SENDER,sender);
	}

	/**
	 * @return
	 */
	public String getRecipients() {
		return get(RECIPIENTS);
	}

	/**
	 * @param recipients
	 */
	public void setRecipients(String recipients) {
		put(RECIPIENTS, recipients);
	}

	/**
	 * @return
	 */
	public String getSubject() {
		return get(SUBJECT);
	}

	/**
	 * @param subject
	 */
	public void setSubject(String subject) {
		put(SUBJECT,subject);
	}

	/**
	 * @return
	 */
	public String getText() {
		return get(TEXT);
	}

	/**
	 * @param text
	 */
	public void setText(String text) {
		put(TEXT,text);
	}

	/**
	 * @return
	 */
	public String getOutputService() {
		return get(OUTPUT_SERVICE);
	}

	/**
	 * @param outputService
	 */
	public void setOutputService(String outputService) {
		put(OUTPUT_SERVICE,outputService);
	}

}
