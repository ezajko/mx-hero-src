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

package org.mxhero.engine.plugin.postfixconnector.internal.snmp;

import java.io.IOException;
import java.io.InputStream;

import org.mailster.smtp.api.SessionContext;
import org.mailster.smtp.api.listener.MessageListener;
import org.mailster.smtp.command.impl.MailCommand;
import org.mxhero.engine.commons.connector.InputService;
import org.mxhero.engine.commons.connector.QueueFullException;
import org.mxhero.engine.commons.finders.UserFinder;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.util.LogMail;
import org.mxhero.engine.plugin.postfixconnector.internal.ConnectorProperties;
import org.mxhero.engine.plugin.postfixconnector.service.PostFixConnectorOutputService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mmarmol This class just takes the mails and added it to the queue
 *         creating a copy of the Stream.
 */
public final class SMTPMessageListener implements MessageListener {

	private static Logger log = LoggerFactory
			.getLogger(SMTPMessageListener.class);

	private ConnectorProperties properties;

	private UserFinder userFinderService;

	private InputService service;

	/**
	 * @see org.mailster.smtp.api.listener.MessageListener#accept(org.mailster.smtp.api.SessionContext,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public boolean accept(SessionContext ctx, String from, String recipient) {
		return true;
	}


	/**
	 * @see org.mailster.smtp.api.listener.MessageListener#deliver(org.mailster.smtp.api.SessionContext,
	 *      java.lang.String, java.lang.String, java.io.InputStream)
	 */
	@Override
	public void deliver(SessionContext ctx, String from, String recipient,
			InputStream data, String ret, String notify) throws IOException {

		MimeMail mail =null;
		try {
			mail = new MimeMail(from, recipient, data,
					PostFixConnectorOutputService.class.getName());
			mail.getMessage().removeHeader(ConnectorProperties.RET_HEADER);
			mail.getMessage().removeHeader(ConnectorProperties.NOTIFY_HEADER);
			mail.getMessage().removeHeader(ConnectorProperties.ID_HEADER);
			if(notify!=null){
				if(ret==null){
					ret=MailCommand.RET_HDRS;
				}
				mail.getMessage().addHeader(ConnectorProperties.RET_HEADER, ret);
				mail.getMessage().addHeader(ConnectorProperties.NOTIFY_HEADER, notify);
				mail.getMessage().addHeader(ConnectorProperties.ID_HEADER, mail.getTime().getTime()+"-"+mail.getSequence());
				log.trace("ret:"+ret+" notify:"+notify);
			}
			mail.getMessage().saveChanges();
		} catch (Exception e1) {
			log.error("email from:"+from+" to:"+recipient+" error="+e1.getMessage());
			if(log.isDebugEnabled()){
				LogMail.saveErrorMail(mail.getMessage(),
						getProperties().getErrorPrefix(),
						getProperties().getErrorSuffix(),
						getProperties().getErrorFolder());
			}
			throw new IOException(e1);
		}
		log.trace("Mail received:" + mail);

		try {
			service.addMail(mail);
		} catch (QueueFullException e) {
			log.error("email from:"+from+" to:"+recipient+" error="+e.getMessage());
			throw new IOException(e);
		} catch (Exception e1){
			log.error("email from:"+from+" to:"+recipient+" error="+e1.getMessage());
			throw new IOException(e1);
		}
		log.trace("Mail added:" + mail);

	}


	/**
	 * @return the properties
	 */
	public ConnectorProperties getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(ConnectorProperties properties) {
		this.properties = properties;
	}

	public UserFinder getUserFinderService() {
		return userFinderService;
	}

	public void setUserFinderService(UserFinder userFinderService) {
		this.userFinderService = userFinderService;
	}

	public InputService getService() {
		return service;
	}

	public void setService(InputService service) {
		this.service = service;
	}

}
