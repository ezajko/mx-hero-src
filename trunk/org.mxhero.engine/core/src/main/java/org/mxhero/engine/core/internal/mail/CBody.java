package org.mxhero.engine.core.internal.mail;

import java.util.regex.Pattern;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.api.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CBody implements Body{

	private static Logger log = LoggerFactory.getLogger(CBody.class);
	
	private MimeMail mimeMail;

	public CBody(MimeMail mimeMail) {
		this.mimeMail = mimeMail;
	}

	@Override
	public String getText() {
		try{
			return MailUtils.getText(this.mimeMail.getMessage(),MailUtils.TEXT_PLAIN_TYPE,false);
		}catch(Exception e){
			log.warn(e.getMessage());
			return "";
		}
	}

	@Override
	public String getHtmlText() {
		try{
			return MailUtils.getText(this.mimeMail.getMessage(),MailUtils.TEXT_HTML_TYPE,false);
		}catch(Exception e){
			log.warn(e.getMessage());
			return "";
		}
	}

	@Override
	public String getDeepText() {
		try{
			return MailUtils.getText(this.mimeMail.getMessage(),MailUtils.TEXT_PLAIN_TYPE,true);
		}catch(Exception e){
			log.warn(e.getMessage());
			return "";
		}
	}

	@Override
	public String getDeepHtmlText() {
		try{
			return MailUtils.getText(this.mimeMail.getMessage(),MailUtils.TEXT_PLAIN_TYPE,true);
		}catch(Exception e){
			log.warn(e.getMessage());
			return "";
		}
	}

	@Override
	public boolean textHasAny(String... words) {
		String text = this.getText();
		for (String word : words){
			if(Pattern.compile("(\\W|^)"+word+"(\\W|$)", Pattern.CASE_INSENSITIVE).matcher(text).find()){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean textHasAll(String... words) {
		String text = this.getText();
		for (String word : words){
			if(!Pattern.compile("(\\W|^)"+word+"(\\W|$)", Pattern.CASE_INSENSITIVE).matcher(text).find()){
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean htmlTextHasAny(String... words) {
		String text = this.getHtmlText();
		for (String word : words){
			if(Pattern.compile("(\\W|^)"+word+"(\\W|$)", Pattern.CASE_INSENSITIVE).matcher(text).find()){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean htmlTextHasAll(String... words) {
		String text = this.getHtmlText();
		for (String word : words){
			if(!Pattern.compile("(\\W|^)"+word+"(\\W|$)", Pattern.CASE_INSENSITIVE).matcher(text).find()){
				return false;
			}
		}
		return true;
	}

	@Override
	public void addText(String text, AddTextPosition position,
			AddTextPartType type) {
		try {
			MailUtils.addText(mimeMail.getMessage(), text, position, type);
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
	}

}
