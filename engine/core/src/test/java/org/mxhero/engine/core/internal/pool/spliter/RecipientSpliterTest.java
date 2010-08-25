package org.mxhero.engine.core.internal.pool.spliter;

import java.util.Arrays;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import junit.framework.Assert;

import org.junit.Test;
import org.mxhero.engine.domain.mail.MimeMail;

public class RecipientSpliterTest {

	@Test
	public void testSplit() throws MessagingException{
		RecipientSpliter spliter = new RecipientSpliter();
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(null));
		message.setSubject("test");
		message.setText("test");
		message.setSender(new InternetAddress("tes@mail.com"));
		message.setFrom(new InternetAddress("tes@mail.com"));
		Address[] address = new Address[2];
		address[0]= new InternetAddress("tes@mail.com");
		address[1]= new InternetAddress("other@mail.com");
		message.setRecipients(RecipientType.TO,address);
		MimeMail mail = new MimeMail("test@mail.com",  Arrays.asList("test@mail.com,other@mail.com".split(",")), message, "service");
		Assert.assertEquals(2, spliter.split(mail).size());
	}
	
}
