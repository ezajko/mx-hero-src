package org.mxhero.engine.commons.mail.business;

import java.util.Collection;

/**
 * Represents the body of a mail so it can be used in rules.
 * @author mmarmol
 */
public class Body {

	private String text;
	
	private String htmlText;

	private String deepText;
	
	private String deepHtmlText;

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the htmlText
	 */
	public String getHtmlText() {
		return htmlText;
	}

	/**
	 * @param htmlText the htmlText to set
	 */
	public void setHtmlText(String htmlText) {
		this.htmlText = htmlText;
	}

	/**
	 * @return the deepText
	 */
	public String getDeepText() {
		return deepText;
	}

	/**
	 * @param deepText the deepText to set
	 */
	public void setDeepText(String deepText) {
		this.deepText = deepText;
	}

	/**
	 * @return the deepHtmlText
	 */
	public String getDeepHtmlText() {
		return deepHtmlText;
	}

	/**
	 * @param deepHtmlText the deepHtmlText to set
	 */
	public void setDeepHtmlText(String deepHtmlText) {
		this.deepHtmlText = deepHtmlText;
	}
	
	public boolean textHasAny(Collection<String> words){
		return false;
	}
	
	public boolean textHasAll(Collection<String> words){
		return false;
	}
	
	public boolean htmlTextHasAny(Collection<String> words){
		return false;
	}
	
	public boolean htmlTextHasAll(Collection<String> words){

		return false;
	}
}
