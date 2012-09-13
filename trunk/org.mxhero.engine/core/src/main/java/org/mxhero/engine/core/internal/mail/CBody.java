/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mxhero.engine.core.internal.mail;

import java.util.regex.Pattern;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.api.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mmarmol
 *
 */
public class CBody implements Body{

	private static Logger log = LoggerFactory.getLogger(CBody.class);
	
	private MimeMail mimeMail;

	/**
	 * @param mimeMail
	 */
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
