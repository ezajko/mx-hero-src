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

package org.mxhero.engine.plugin.gsync.internal.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.mxhero.engine.commons.connector.InputService;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.api.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MailSender {

	private static Logger log = LoggerFactory.getLogger(MailSender.class);
	
	public static void sendMail(InputService inputService, String notifyEmail, String errorMessage, String senderMail, String outputService){
			try{
				InternetAddress recipient = new InternetAddress(notifyEmail,false);
				InternetAddress sender = new InternetAddress(senderMail);
				
				MimeMessage newMessage = new MimeMessage(Session.getDefaultInstance(new Properties()));
				newMessage.setSender(sender);
				newMessage.setFrom(sender);
				newMessage.setReplyTo(new InternetAddress[] {sender});
				newMessage.setRecipient(RecipientType.TO, recipient);
				newMessage.setSubject("AD/LDAP SYNC ERROR");
				newMessage.setText(errorMessage);
				newMessage.setSentDate(Calendar.getInstance().getTime());
				newMessage.saveChanges();
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				newMessage.writeTo(os);
				ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
				MimeMail newMail = new MimeMail(sender.toString(), recipient.toString(),
						is, outputService);
				newMail.setPhase(Mail.Phase.send);
				inputService.addMail(newMail);
			}catch(Exception e){
				log.warn("Error while sending email",e);
			}
	}
	
}
