package org.mxhero.engine.plugin.basecommands.command.reply;

import org.mxhero.engine.commons.mail.command.NamedParameters;

/**
 * @author mmarmol
 *
 */
public class ReplyParameters extends NamedParameters {

	public static final String SENDER = "sender";
	public static final String RECIPIENT = "recipient";
	public static final String SUBJECT = "subject";
	public static final String PLAIN_TEXT = "plainText";
	public static final String HTML_TEXT = "htmlText";
	public static final String OUTPUT_SERVICE = "outputService";
	public static String INCLUDE_MESSAGE = "includeMessage";

	/**
	 * 
	 */
	public ReplyParameters() {
	}

	public ReplyParameters(String sender, String plainText, String htmlText) {
		put(SENDER,sender);
		put(PLAIN_TEXT,plainText);
		put(HTML_TEXT,htmlText);
	}
	
	/**
	 * @param parameters
	 */
	public ReplyParameters(NamedParameters parameters) {
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
		put(SENDER, sender);
	}

	/**
	 * @return
	 */
	public String getRecipient() {
		return get(RECIPIENT);
	}

	/**
	 * @param recipient
	 */
	public void setRecipient(String recipient) {
		put(RECIPIENT, recipient);
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
		put(SUBJECT, subject);
	}

	/**
	 * @return
	 */
	public String getPlainText() {
		return get(PLAIN_TEXT);
	}

	/**
	 * @param plainText
	 */
	public void setPlainText(String plainText) {
		put(PLAIN_TEXT, plainText);
	}

	/**
	 * @return
	 */
	public String getHtmlText() {
		return get(HTML_TEXT);
	}

	/**
	 * @param htmlText
	 */
	public void setHtmlText(String htmlText) {
		put(HTML_TEXT, htmlText);
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
		put(OUTPUT_SERVICE, outputService);
	}

	/**
	 * @return
	 */
	public Boolean getIncludeMessage() {
		return get(INCLUDE_MESSAGE);
	}

	/**
	 * @param includeMessage
	 */
	public void setIncludeMessage(Boolean includeMessage) {
		put(INCLUDE_MESSAGE, includeMessage);
	}
	
}
