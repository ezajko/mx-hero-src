package org.mxhero.engine.core.mail.filter;

import java.util.Arrays;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import junit.framework.Assert;

import org.junit.Test;
import org.mxhero.engine.domain.mail.MimeMail;

public class AddRemoveBccHeaderTest {

	@Test
	public void testAddRemoveBcc() throws MessagingException{
		
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setSubject("test");
		message.setText("test");
		message.setSender(new InternetAddress("tes@mail.com"));
		message.setFrom(new InternetAddress("tes@mail.com"));
		Address[] address = new Address[1];
		address[0]= new InternetAddress("test@mail.com");
		message.setRecipients(RecipientType.TO,address);
		message.saveChanges();
		MimeMail mail = new MimeMail("test@mail.com",  Arrays.asList("test@mail.com,other@mail.com,andother@mail.com".split(",")), message, "service");

		AddBccHeader add = new AddBccHeader();
		for(String email : mail.getRecipients()){
			System.out.println("["+email+"]");
		}
		add.process(mail);
		Assert.assertTrue(mail.getMessage().getAllRecipients().length==mail.getRecipients().size());
		
		RemoveBccHeader remove = new RemoveBccHeader();
		remove.process(mail);
		Assert.assertTrue(address.length==mail.getMessage().getAllRecipients().length);
	}
	
}
