package org.mxhero.engine.plugin.basecommands.internal.command;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeMessage.RecipientType;

import junit.framework.Assert;

import org.junit.Test;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.command.Result;
import org.mxhero.engine.plugin.basecommands.internal.command.RemoveByExtension;

public class RemoveByExtensionTest {

	@Test
	public void wrongParams(){
		Assert.assertFalse((new RemoveByExtension().exec(null)).isTrue());
		Assert.assertFalse((new RemoveByExtension().exec(null,(String[])null)).isTrue());
		Assert.assertFalse((new RemoveByExtension().exec(null,null,null)).isTrue());
		Assert.assertFalse((new RemoveByExtension().exec(null,"","")).isTrue());
	}
	
	@Test
	public void removeFile() throws MessagingException, URISyntaxException{
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setSender(new InternetAddress("mmarmol@mxhero.com"));
		message.setFrom(new InternetAddress("mmarmol@mxhero.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(
				"mmarmol@mxhero.com"));
		message.setSubject("subject");


		MimeBodyPart mbp1 = new MimeBodyPart();
		mbp1.setText("text");
		MimeBodyPart mbp2 = new MimeBodyPart();
		FileDataSource fds2 = new FileDataSource(new File(this.getClass()
				.getClassLoader().getResource("log4j.properties").toURI()));
		mbp2.setDataHandler(new DataHandler(fds2));
		mbp2.setFileName(fds2.getName());
		mbp2.setDisposition(Part.ATTACHMENT);
		MimeMultipart mp = new MimeMultipart();
		mp.addBodyPart(mbp1);
		mp.addBodyPart(mbp2);
		message.setContent(mp);
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();

		MimeMail mail = new MimeMail("mmarmol@mxhero.com", Arrays.asList("mmarmol@mxhero.com".split(",")), message, "service");
		Result result = new RemoveByExtension().exec(mail,"properties");
		Assert.assertTrue(result.isTrue());
		Assert.assertTrue(result.getLongField()==1);
		Assert.assertTrue(result.getText()!=null);
	}
	
	@Test
	public void dontRemove() throws MessagingException, URISyntaxException{
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setSender(new InternetAddress("mmarmol@mxhero.com"));
		message.setFrom(new InternetAddress("mmarmol@mxhero.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(
				"mmarmol@mxhero.com"));
		message.setSubject("subject");


		MimeBodyPart mbp1 = new MimeBodyPart();
		mbp1.setText("text");
		MimeBodyPart mbp2 = new MimeBodyPart();
		FileDataSource fds2 = new FileDataSource(new File(this.getClass()
				.getClassLoader().getResource("log4j.properties").toURI()));
		mbp2.setDataHandler(new DataHandler(fds2));
		mbp2.setFileName(fds2.getName());
		mbp2.setDisposition(Part.ATTACHMENT);
		MimeMultipart mp = new MimeMultipart();
		mp.addBodyPart(mbp1);
		mp.addBodyPart(mbp2);
		message.setContent(mp);
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();

		MimeMail mail = new MimeMail("mmarmol@mxhero.com", Arrays.asList("mmarmol@mxhero.com".split(",")), message, "service");
		
		Assert.assertFalse((new RemoveByExtension().exec(mail,"txt")).isTrue());
	}
}
