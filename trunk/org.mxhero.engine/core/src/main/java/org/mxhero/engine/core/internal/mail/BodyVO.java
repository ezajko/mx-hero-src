package org.mxhero.engine.core.internal.mail;

import java.util.Collection;
import java.util.regex.Pattern;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.business.Body;
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
		try{
			return MailUtils.getText(this.mimeMail.getMessage(),MailUtils.TEXT_PLAIN_TYPE,false);
		}catch(Exception e){
			return "";
		}
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
		try{
			return MailUtils.getText(this.mimeMail.getMessage(),MailUtils.TEXT_HTML_TYPE,false);
		}catch(Exception e){
			return "";
		}
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
		try{
			return MailUtils.getText(this.mimeMail.getMessage(),MailUtils.TEXT_PLAIN_TYPE,true);
		}catch(Exception e){
			return "";
		}
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
		try{
			return MailUtils.getText(this.mimeMail.getMessage(),MailUtils.TEXT_PLAIN_TYPE,true);
		}catch(Exception e){
			return "";
		}
	}

	/**
	 * Should not be call, just override it for drools.
	 * @param deepHtmlText the deepHtmlText to set
	 */
	@Override
	public void setDeepHtmlText(String deepHtmlText) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	@Override
	public boolean textHasAny(Collection<String> words){
		String text = this.getText();
		for (String word : words){
			if(Pattern.compile("(\\W|^)"+word+"(\\W|$)", Pattern.CASE_INSENSITIVE).matcher(text).find()){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean textHasAll(Collection<String> words){
		String text = this.getText();
		for (String word : words){
			if(!Pattern.compile("(\\W|^)"+word+"(\\W|$)", Pattern.CASE_INSENSITIVE).matcher(text).find()){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean htmlTextHasAny(Collection<String> words){
		String text = this.getHtmlText();
		for (String word : words){
			if(Pattern.compile("(\\W|^)"+word+"(\\W|$)", Pattern.CASE_INSENSITIVE).matcher(text).find()){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean htmlTextHasAll(Collection<String> words){
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
