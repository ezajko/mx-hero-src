package org.mxhero.engine.plugin.basecommands.internal.command;

import java.util.Arrays;
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
import org.mxhero.engine.plugin.basecommands.internal.command.AddRecipientImpl;

public class AddRecipientTest {

	@Test
	public void wrongParams(){
		Assert.assertFalse((new AddRecipientImpl().exec(null)).isTrue());
		Assert.assertFalse((new AddRecipientImpl().exec(null,(String[])null)).isTrue());
		Assert.assertFalse((new AddRecipientImpl().exec(null,null,null)).isTrue());
		Assert.assertFalse((new AddRecipientImpl().exec(null,"",null)).isTrue());
		Assert.assertFalse((new AddRecipientImpl().exec(null,null,"")).isTrue());
		Assert.assertFalse((new AddRecipientImpl().exec(null,"","")).isTrue());
	}

	@Test
	public void addTo() throws AddressException, MessagingException{
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setSender(new InternetAddress("mmarmol@mxhero.com"));
		message.setFrom(new InternetAddress("mmarmol@mxhero.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(
				"mmarmol@mxhero.com"));
		message.setSubject("subject");
		message.setText("\n\r TEXTO \n\r");
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();
		
		MimeMail mail = new MimeMail("mmarmol@mxhero.com", Arrays.asList("mmarmol@mxhero.com".split(",")), message, "service");

		Assert.assertTrue(new AddRecipientImpl().exec(mail, "to","other@gmail.com").isTrue());
		Assert.assertTrue(mail.getMessage().getRecipients(RecipientType.TO).length==2);
	}
	
	@Test
	public void addCC() throws AddressException, MessagingException{
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(null));
		message.setSender(new InternetAddress("mmarmol@mxhero.com"));
		message.setFrom(new InternetAddress("mmarmol@mxhero.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(
				"mmarmol@mxhero.com"));
		message.setSubject("subject");
		message.setText("\n\r TEXTO \n\r");
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();
		
		MimeMail mail = new MimeMail("mmarmol@mxhero.com", Arrays.asList("mmarmol@mxhero.com".split(",")), message, "service");

		Assert.assertTrue(new AddRecipientImpl().exec(mail, "cc","other@gmail.com").isTrue());
		Assert.assertTrue(mail.getMessage().getRecipients(RecipientType.CC).length==1);
	}
	
	@Test
	public void addBCC() throws AddressException, MessagingException{
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setSender(new InternetAddress("mmarmol@mxhero.com"));
		message.setFrom(new InternetAddress("mmarmol@mxhero.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(
				"mmarmol@mxhero.com"));
		message.setSubject("subject");
		message.setText("\n\r TEXTO \n\r");
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();
		
		MimeMail mail = new MimeMail("mmarmol@mxhero.com", Arrays.asList("mmarmol@mxhero.com".split(",")), message, "service");

		Assert.assertTrue(new AddRecipientImpl().exec(mail, "bcc","other@gmail.com").isTrue());
		Assert.assertTrue(mail.getMessage().getRecipients(RecipientType.BCC).length==1);
	}
	
	@Test
	public void addNone() throws AddressException, MessagingException{
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(null));
		message.setSender(new InternetAddress("mmarmol@mxhero.com"));
		message.setFrom(new InternetAddress("mmarmol@mxhero.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(
				"mmarmol@mxhero.com"));
		message.setSubject("subject");
		message.setText("\n\r TEXTO \n\r");
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();
		
		MimeMail mail = new MimeMail("mmarmol@mxhero.com", Arrays.asList("mmarmol@mxhero.com".split(",")), message, "service");

		Assert.assertTrue(new AddRecipientImpl().exec(mail, "none","other@gmail.com").isTrue());
		Assert.assertTrue(new AddRecipientImpl().exec(mail, "none","other@gmail.com").isTrue());
		Assert.assertTrue(mail.getRecipients().size()==3);
	}
}
