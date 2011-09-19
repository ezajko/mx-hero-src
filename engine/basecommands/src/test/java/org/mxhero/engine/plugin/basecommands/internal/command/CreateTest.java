package org.mxhero.engine.plugin.basecommands.internal.command;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import org.mxhero.engine.domain.connector.InputService;
import org.mxhero.engine.domain.mail.MimeMail;

public class CreateTest {

	@Test
	public void invalidParams() throws AddressException, MessagingException, IOException{
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setSender(new InternetAddress("mmarmol@mxhero.com"));
		message.setFrom(new InternetAddress("mmarmol@mxhero.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(
				"mmarmol@mxhero.com"));
		message.setSubject("subject");
		message.setText("\n\r TEXTO \n\r");
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		message.writeTo(os);
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		MimeMail mail = new MimeMail("mmarmol@mxhero.com", "mmarmol@mxhero.com", is, "service");
		
		Assert.assertFalse((new CreateImpl().exec(mail)).isTrue());
		Assert.assertFalse((new CreateImpl().exec(mail,(String[])null)).isTrue());
		Assert.assertFalse((new CreateImpl().exec(mail,null,null)).isTrue());
		Assert.assertFalse((new CreateImpl().exec(mail,null,null,null)).isTrue());
		Assert.assertFalse((new CreateImpl().exec(mail,null,null,null,null)).isTrue());
		Assert.assertFalse((new CreateImpl().exec(mail,"wronghase","","","")).isTrue());
		Assert.assertFalse((new CreateImpl().exec(mail,"","","")).isTrue());
		Assert.assertFalse((new CreateImpl().exec(mail,"","","","")).isTrue());
		Assert.assertFalse((new CreateImpl().exec(mail,"","","","","")).isTrue());
		Assert.assertFalse((new CreateImpl().exec(mail,"valid@mxhero.com","","","","")).isTrue());
	}
	
	@Test
	public void testOk() throws AddressException, MessagingException, IOException{
		CreateImpl create = new CreateImpl();
		create.setService(new InputService() {
			
			@Override
			public void addMail(MimeMail mail) {
				// just for test
				
			}
		});
		
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setSender(new InternetAddress("mmarmol@mxhero.com"));
		message.setFrom(new InternetAddress("mmarmol@mxhero.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(
				"mmarmol@mxhero.com"));
		message.setSubject("subject");
		message.setText("\n\r TEXTO \n\r");
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		message.writeTo(os);
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		MimeMail mail = new MimeMail("mmarmol@mxhero.com", "mmarmol@mxhero.com", is, "service");
		
		Assert.assertTrue(create.exec(mail, "sender@mxhero.com","recipient@mxhero.com","subject","text","service").isTrue());
	}
}
