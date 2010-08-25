package org.mxhero.engine.plugin.basecommands.internal.command;

import java.util.Arrays;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import junit.framework.Assert;

import org.junit.Test;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.plugin.basecommands.internal.command.GetSize;

public class GetSizeTest {

	@Test
	public void testSize() throws MessagingException{
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setSender(new InternetAddress("test@test.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress("dest@dest.com"));
		message.setSubject("test");
		message.setText("some text");
		MimeMail mail = new MimeMail("test@test.com",
				Arrays.asList("dest@dest.com".split(",")),
						message,
						"service");
		
		Assert.assertTrue(new GetSize().exec(mail).isTrue());
		Assert.assertTrue(new GetSize().exec(mail,"k").isTrue());
		Assert.assertTrue(new GetSize().exec(mail,"K").isTrue());
		Assert.assertTrue(new GetSize().exec(mail,"b").isTrue());
		Assert.assertTrue(new GetSize().exec(mail,"B").isTrue());
		Assert.assertTrue(new GetSize().exec(mail,"m").isTrue());
		Assert.assertTrue(new GetSize().exec(mail,"M").isTrue());
		
		Assert.assertFalse(new GetSize().exec(mail,"k ").isTrue());
		Assert.assertFalse(new GetSize().exec(mail," k").isTrue());
		Assert.assertFalse(new GetSize().exec(mail,"kbytes ").isTrue());
	}
	
}
