package org.mxhero.engine.plugin.basecommands.internal.command;

import java.io.IOException;
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
import org.mxhero.engine.plugin.basecommands.internal.command.AddText;

public class AddTextTest {

	@Test
	public void wrongParams(){
		Assert.assertFalse((new AddText().exec(null)).isTrue());
		Assert.assertFalse((new AddText().exec(null,(String[])null)).isTrue());
		Assert.assertFalse((new AddText().exec(null,null,null)).isTrue());
		Assert.assertFalse((new AddText().exec(null,"",null)).isTrue());
		Assert.assertFalse((new AddText().exec(null,null,"")).isTrue());
		Assert.assertFalse((new AddText().exec(null,"","")).isTrue());
	}
	
	@Test
	public void addTextTop() throws AddressException, MessagingException, IOException{
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
		
		Assert.assertTrue((new AddText().exec(mail,"ADDED ON TOP","top")).isTrue());
		Assert.assertTrue(mail.getMessage().getContent().toString().contains("ADDED ON TOP"));
	}
	
	@Test
	public void addTextBottom() throws AddressException, MessagingException, IOException{
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
		
		Assert.assertTrue((new AddText().exec(mail,"ADDED ON BOTTOM","bottom")).isTrue());
		Assert.assertTrue(mail.getMessage().getContent().toString().contains("ADDED ON BOTTOM"));
	}

}
