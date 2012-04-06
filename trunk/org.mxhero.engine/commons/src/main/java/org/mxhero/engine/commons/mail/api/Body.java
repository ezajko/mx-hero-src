package org.mxhero.engine.commons.mail.api;

/**
 * Represents the body of a mail so it can be used in rules.
 * @author mmarmol
 */
public interface Body {

	public enum AddTextPosition {
        botton, top
    }
	
	public enum AddTextPartType {
        plain, html, both
    }
	
	/**
	 * @return the text
	 */
	public String getText();

	/**
	 * @return the htmlText
	 */
	public String getHtmlText();


	/**
	 * @return the deepText
	 */
	public String getDeepText();

	/**
	 * @return the deepHtmlText
	 */
	public String getDeepHtmlText();
	
	/**
	 * @param word
	 * @return
	 */
	public boolean textHasAny(String... words);
	
	/**
	 * @param word
	 * @return
	 */
	public boolean textHasAll(String... words);
	
	/**
	 * @param word
	 * @return
	 */
	public boolean htmlTextHasAny(String... words);
	
	/**
	 * @param words
	 * @return
	 */
	public boolean htmlTextHasAll(String... words);
	
	/**
	 * @param text
	 * @param position
	 * @param type
	 * @return
	 */
	public void addText(String text, AddTextPosition position, AddTextPartType type);
	
}
