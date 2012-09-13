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
