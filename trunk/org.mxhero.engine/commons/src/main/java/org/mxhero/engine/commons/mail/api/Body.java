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
