package org.mxhero.engine.plugin.basecommands.internal.command;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import junit.framework.Assert;

import org.junit.Test;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.plugin.basecommands.internal.command.GetSizeImpl;

public class GetSizeTest {

	@Test
	public void testSize() throws MessagingException, IOException{
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setSender(new InternetAddress("test@test.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress("dest@dest.com"));
		message.setSubject("test");
		message.setText("some text");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		message.writeTo(os);
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		MimeMail mail = new MimeMail("test@test.com","dest@dest.com",
						is,
						"service");
		
		Assert.assertTrue(new GetSizeImpl().exec(mail).isTrue());
		Assert.assertTrue(new GetSizeImpl().exec(mail,"k").isTrue());
		Assert.assertTrue(new GetSizeImpl().exec(mail,"K").isTrue());
		Assert.assertTrue(new GetSizeImpl().exec(mail,"b").isTrue());
		Assert.assertTrue(new GetSizeImpl().exec(mail,"B").isTrue());
		Assert.assertTrue(new GetSizeImpl().exec(mail,"m").isTrue());
		Assert.assertTrue(new GetSizeImpl().exec(mail,"M").isTrue());
		
		Assert.assertFalse(new GetSizeImpl().exec(mail,"k ").isTrue());
		Assert.assertFalse(new GetSizeImpl().exec(mail," k").isTrue());
		Assert.assertFalse(new GetSizeImpl().exec(mail,"kbytes ").isTrue());
	}
	
}
