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

package org.mxhero.engine.plugin.attachmentlink.alcommand;

import org.mxhero.engine.commons.mail.command.NamedParameters;

/**
 * @author mmarmol
 *
 */
public class ALCommandParameters extends NamedParameters{

	public static final String LOCALE = "locale";
	public static final String NOTIFY = "notify";
	public static final String NOTIFY_MESSAGE = "notifyMessage";
	public static final String NOTIFY_MESSAGE_HTML = "notifyMessageHtml";	
	
	/**
	 * 
	 */
	public ALCommandParameters(){
		super();
	}

	public ALCommandParameters(String locale, Boolean notify, String notifyMessage, String notifyMessageHtml){
		super();
		this.setLocale(locale);
		this.setNotify(notify);
		this.setNotifyMessage(notifyMessage);
		this.setNotifyMessageHtml(notifyMessageHtml);
	}
	
	/**
	 * @param parameters
	 */
	public ALCommandParameters(NamedParameters parameters){
		super();
		this.nameToInstance.putAll(parameters.getNameToInstance());
	}
	
	/**
	 * @return
	 */
	public String getLocale() {
		return get(LOCALE);
	}

	/**
	 * @param locale
	 */
	public void setLocale(String locale) {
		put(LOCALE, locale);
	}

	/**
	 * @return
	 */
	public Boolean getNotify() {
		return get(NOTIFY);
	}

	/**
	 * @param notify
	 */
	public void setNotify(Boolean notify) {
		put(NOTIFY, notify);
	}

	/**
	 * @return
	 */
	public String getNotifyMessage() {
		return get(NOTIFY_MESSAGE);
	}

	/**
	 * @param notifyMessage
	 */
	public void setNotifyMessage(String notifyMessage) {
		put(NOTIFY_MESSAGE, notifyMessage);
	}

	/**
	 * @return
	 */
	public String getNotifyMessageHtml() {
		return get(NOTIFY_MESSAGE_HTML);
	}

	/**
	 * @param notifyMessage
	 */
	public void setNotifyMessageHtml(String notifyMessageHtml) {
		put(NOTIFY_MESSAGE_HTML, notifyMessageHtml);
	}

}
