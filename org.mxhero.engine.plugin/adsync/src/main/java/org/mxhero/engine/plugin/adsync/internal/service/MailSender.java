package org.mxhero.engine.plugin.adsync.internal.service;

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
import org.mxhero.engine.commons.mail.business.RulePhase;
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
				newMail.setPhase(RulePhase.SEND);
				inputService.addMail(newMail);
			}catch(Exception e){
				log.warn("Error while sending email",e);
			}
	}
	
}
