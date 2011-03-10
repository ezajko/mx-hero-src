package org.mxhero.engine.core.mail;

import java.util.regex.Pattern;

import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Override the original Body to implement MimeMessage related logic.
 * @author mmarmol
 */
public class BodyVO extends Body{

	private static Logger log = LoggerFactory.getLogger(BodyVO.class);
	
	private MimeMail mimeMail;
	
	/**
	 * @param mimeMail
	 */
	public BodyVO(MimeMail mimeMail){
		this.mimeMail = mimeMail;
	}
	
	/**
	 * @return the text
	 */
	@Override
	public String getText() {
		return MailUtils.getText(this.mimeMail.getMessage(),MailUtils.TEXT_PLAIN_TYPE,false);
	}

	/**
	 *  Should not be call, just override it for drools.
	 * @param text the text to set
	 */
	@Override
	public void setText(String text) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	/**
	 * @return the htmlText
	 */
	@Override
	public String getHtmlText() {
		return MailUtils.getText(this.mimeMail.getMessage(),MailUtils.TEXT_HTML_TYPE,false);
	}

	/**
	 *  Should not be call, just override it for drools.
	 * @param htmlText the htmlText to set
	 */
	@Override
	public void setHtmlText(String htmlText) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	/**
	 * @return the deepText
	 */
	@Override
	public String getDeepText() {
		return MailUtils.getText(this.mimeMail.getMessage(),MailUtils.TEXT_PLAIN_TYPE,true);
	}

	/**
	 * Should not be call, just override it for drools.
	 * @param deepText the deepText to set
	 */
	@Override
	public void setDeepText(String deepText) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	/**
	 * @return the deepHtmlText
	 */
	@Override
	public String getDeepHtmlText() {
		return MailUtils.getText(this.mimeMail.getMessage(),MailUtils.TEXT_PLAIN_TYPE,true);
	}

	/**
	 * Should not be call, just override it for drools.
	 * @param deepHtmlText the deepHtmlText to set
	 */
	@Override
	public void setDeepHtmlText(String deepHtmlText) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	
	public boolean textHasAny(String[] words){
		String text = this.getText();
		for (String word : words){
			if(Pattern.compile("(\\W|^)"+word+"(\\W|$)", Pattern.CASE_INSENSITIVE).matcher(text).find()){
				return true;
			}
		}
		return false;
	}
	
	public boolean textHasAll(String[] words){
		String text = this.getText();
		for (String word : words){
			if(!Pattern.compile("(\\W|^)"+word+"(\\W|$)", Pattern.CASE_INSENSITIVE).matcher(text).find()){
				return false;
			}
		}
		return true;
	}
	
	public boolean htmlTextHasAny(String[] words){
		String text = this.getHtmlText();
		for (String word : words){
			if(Pattern.compile("(\\W|^)"+word+"(\\W|$)", Pattern.CASE_INSENSITIVE).matcher(text).find()){
				return true;
			}
		}
		return false;
	}
	
	public boolean htmlTextHasAll(String[] words){
		String text = this.getHtmlText();
		for (String word : words){
			if(!Pattern.compile("(\\W|^)"+word+"(\\W|$)", Pattern.CASE_INSENSITIVE).matcher(text).find()){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BodyVO [getHtmlText()=").append(getHtmlText()).append(
				", getText()=").append(getText()).append("]");
		return builder.toString();
	}

}
