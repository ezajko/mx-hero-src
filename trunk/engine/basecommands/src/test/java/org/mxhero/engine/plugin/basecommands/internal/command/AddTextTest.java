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
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.plugin.basecommands.internal.command.AddTextImpl;

public class AddTextTest {

	@Test
	public void wrongParams(){
		Assert.assertFalse((new AddTextImpl().exec(null)).isTrue());
		Assert.assertFalse((new AddTextImpl().exec(null,(String[])null)).isTrue());
		Assert.assertFalse((new AddTextImpl().exec(null,null,null)).isTrue());
		Assert.assertFalse((new AddTextImpl().exec(null,"",null)).isTrue());
		Assert.assertFalse((new AddTextImpl().exec(null,null,"")).isTrue());
		Assert.assertFalse((new AddTextImpl().exec(null,"","")).isTrue());
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
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		message.writeTo(os);
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		MimeMail mail = new MimeMail("mmarmol@mxhero.com", "mmarmol@mxhero.com", is, "service");
		
		Assert.assertTrue((new AddTextImpl().exec(mail,"ADDED ON TOP","top")).isTrue());
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
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		message.writeTo(os);
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		MimeMail mail = new MimeMail("mmarmol@mxhero.com", "mmarmol@mxhero.com", is, "service");
		
		Assert.assertTrue((new AddTextImpl().exec(mail,"ADDED ON BOTTOM","bottom")).isTrue());
		Assert.assertTrue(mail.getMessage().getContent().toString().contains("ADDED ON BOTTOM"));
	}
	
	@Test
	public void addTextBottomHtlm() throws AddressException, MessagingException, IOException{
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setSender(new InternetAddress("mmarmol@mxhero.com"));
		message.setFrom(new InternetAddress("mmarmol@mxhero.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(
				"mmarmol@mxhero.com"));
		message.setSubject("subject");
		message.setContent("<h1>Hello world</h1>","text/html");
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		message.writeTo(os);
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		MimeMail mail = new MimeMail("mmarmol@mxhero.com","mmarmol@mxhero.com", is, "service");
		
		Assert.assertTrue((new AddTextImpl().exec(mail,"ADDED ON BOTTOM","bottom","html")).isTrue());
		Assert.assertTrue(mail.getMessage().getContent().toString().contains("ADDED ON BOTTOM"));
		System.out.println(mail.getMessage().getContent().toString());
	}
	
	@Test
	public void addTextTopHtml() throws AddressException, MessagingException, IOException{
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setSender(new InternetAddress("mmarmol@mxhero.com"));
		message.setFrom(new InternetAddress("mmarmol@mxhero.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(
				"mmarmol@mxhero.com"));
		message.setSubject("subject");
		message.setContent("<h1 id='sss'>Hello world</h1>","text/html");
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		message.writeTo(os);
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		MimeMail mail = new MimeMail("mmarmol@mxhero.com", "mmarmol@mxhero.com", is, "service");
		
		Assert.assertTrue((new AddTextImpl().exec(mail,"<B>ADDED ON TOP</B>","top","hTmL")).isTrue());
		Assert.assertTrue(mail.getMessage().getContent().toString().contains("ADDED ON TOP"));
		System.out.println(mail.getMessage().getContent().toString());
	}

}
