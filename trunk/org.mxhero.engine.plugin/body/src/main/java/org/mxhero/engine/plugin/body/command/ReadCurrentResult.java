package org.mxhero.engine.plugin.body.command;

import org.mxhero.engine.commons.mail.command.Result;

public class ReadCurrentResult extends Result{

	private String plainText;
	
	private String htmlText;
	
	private String htmlAsPlainText;

	public String getPlainText() {
		return plainText;
	}

	public void setPlainText(String plainText) {
		this.plainText = plainText;
	}

	public String getHtmlText() {
		return htmlText;
	}

	public void setHtmlText(String htmlText) {
		this.htmlText = htmlText;
	}

	public String getHtmlAsPlainText() {
		return htmlAsPlainText;
	}

	public void setHtmlAsPlainText(String htmlAsPlainText) {
		this.htmlAsPlainText = htmlAsPlainText;
	}

}
