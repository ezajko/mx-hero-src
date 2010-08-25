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
import org.mxhero.engine.domain.connector.InputService;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.RulePhase;

public class CloneTest {

	@Test
	public void invalidParams() throws AddressException, MessagingException{
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
		
		Assert.assertFalse((new Clone().exec(mail)).isTrue());
		Assert.assertFalse((new Clone().exec(mail,(String[])null)).isTrue());
		Assert.assertFalse((new Clone().exec(mail,null,null)).isTrue());
		Assert.assertFalse((new Clone().exec(mail,null,null,null)).isTrue());
		Assert.assertFalse((new Clone().exec(mail,null,null,null,null)).isTrue());
		Assert.assertFalse((new Clone().exec(mail,"wronghase","","","")).isTrue());
		Assert.assertFalse((new Clone().exec(mail,RulePhase.SEND,"","","")).isTrue());
		Assert.assertFalse((new Clone().exec(mail,RulePhase.SEND,"valid@mxhero.com","","")).isTrue());
		
	}
	
	@Test
	public void testOk() throws AddressException, MessagingException{
		Clone clone = new Clone();
		clone.setService(new InputService() {
			
			@Override
			public void addMail(MimeMail mail) {
				//do nothing, just for test
				
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
		
		MimeMail mail = new MimeMail("mmarmol@mxhero.com", Arrays.asList("mmarmol@mxhero.com".split(",")), message, "service");

		Assert.assertTrue(clone.exec(mail, RulePhase.SEND,"other@mxhero.com","dest@mxhero.com").isTrue());
	}
}
