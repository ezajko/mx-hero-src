package org.mxhero.engine.plugin.basecommands.internal.command;

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
import org.mxhero.engine.domain.mail.business.RulePhase;

public class ReplyTest {

	@Test
	public void testMatch() throws AddressException, MessagingException{
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setSender(new InternetAddress("admin@mxhero.com"));
		message.setFrom(new InternetAddress("admin@mxhero.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(
				"mmarmol@mxhero.com"));
		message.setSubject("subject");
		message.setText("\n\r TEXTO \n\r");
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();
		
		MimeMail mail = new MimeMail("admin@mxhero.com", "mmarmol@mxhero.com", message, "service");
		Assert.assertFalse(new ReplayImpl().exec(mail,"noreply@mxhero.com", "This is the text ${mxsender} ${mxrecipient} \n ${From} \n ${To} \n ${Subject} \n ${Cc} \n ${Bcc}",RulePhase.SEND).isTrue());
	}
	
}
