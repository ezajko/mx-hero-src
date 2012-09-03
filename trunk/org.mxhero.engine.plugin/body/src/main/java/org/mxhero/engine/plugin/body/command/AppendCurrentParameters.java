package org.mxhero.engine.plugin.body.command;

import org.mxhero.engine.commons.mail.command.NamedParameters;

public class AppendCurrentParameters extends NamedParameters{

	public static final String PLAIN_TEXT = "plainText";
	public static final String HTML_TEXT = "htmlText";
	
	public AppendCurrentParameters() {
		super();
	}

	public AppendCurrentParameters(NamedParameters parameters) {
		super();
		this.nameToInstance.putAll(parameters.getNameToInstance());
	}

	public AppendCurrentParameters(String plainText, String htmlText){
		this.setHtmlText(htmlText);
		this.setPlainText(plainText);
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
	
}
