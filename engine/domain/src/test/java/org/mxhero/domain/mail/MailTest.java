package org.mxhero.domain.mail;

import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import junit.framework.Assert;

import org.junit.Test;
import org.mxhero.engine.domain.mail.MimeMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailTest {

	private static Logger log = LoggerFactory.getLogger(MailTest.class);
	
	@Test
	public void testMail() throws MessagingException, IOException{
		MimeMessage messageone = new MimeMessage(Session.getDefaultInstance(new Properties()));
		MimeMail mail = new MimeMail("from","recipient;rec",messageone,"service");
		Assert.assertNotNull(mail.getInitialSender());
		Assert.assertNotNull(mail.getResponseServiceId());
		Assert.assertNotNull(mail.getMessage());
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setSender(new InternetAddress("mmarmol@mxhero.com"));
		message.setFrom(new InternetAddress("mmarmol@mxhero.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(
				"mmarmol@mxhero.com"));
		message.setSubject("subject");
		message.setText("texto");
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();

		MimeMail mailStream = new MimeMail("from","recipient;rec",message,"service");
		log.info(mailStream.toString());
	}

	@Test
	public void testMessageId() throws AddressException, MessagingException{
		
		MimeMessageExt message = new MimeMessageExt(Session.getDefaultInstance(new Properties()));
		message.setSender(new InternetAddress("mmarmol@mxhero.com"));
		message.setFrom(new InternetAddress("mmarmol@mxhero.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(
				"mmarmol@mxhero.com"));
		message.setSubject("subject");
		message.setText("texto");
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();
		
		MimeMail mail = new MimeMail("mmarmol@mxhero.com", "mmarmol@mxhero.com", message, "service");

		System.out.println(mail.getMessage().getMessageID());

		mail.getMessage().saveChanges();
	
		System.out.println(mail.getMessage().getMessageID());

		mail.setMessageId("123123132@localhost.com.ar");
		mail.getMessage().saveChanges();
		
		System.out.println(mail.getMessage().getMessageID());
		
	}
	
	private class MimeMessageExt extends MimeMessage{

		public MimeMessageExt(Session session) throws MessagingException {
			super(session);
		}
		
		public MimeMessageExt(MimeMessage arg0) throws MessagingException {
			super(arg0);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		protected void updateMessageID() throws MessagingException {
			// TODO Auto-generated method stub
			if(getMessageID()==null){
				super.updateMessageID();
			}
			//
		}
		
		@Override
		public void saveChanges() throws MessagingException {
			// TODO Auto-generated method stub
			super.saveChanges();
			
		}
		
	}
	
}
