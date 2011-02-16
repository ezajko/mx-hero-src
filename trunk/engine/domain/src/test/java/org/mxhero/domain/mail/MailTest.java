package org.mxhero.domain.mail;

import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
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
		MimeMail mail = new MimeMail("from","recipient;rec","content".getBytes(),"service");
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
	
}
