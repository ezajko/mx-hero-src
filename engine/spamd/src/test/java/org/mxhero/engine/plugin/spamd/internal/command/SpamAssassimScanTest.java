package org.mxhero.engine.plugin.spamd.internal.command;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import org.junit.runner.RunWith;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.command.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/test-bundle-context.xml"})
public class SpamAssassimScanTest {

	private static Logger log = LoggerFactory.getLogger(SpamAssassimScanTest.class);
	
	@Autowired
	private ApplicationContext ctx = null;
	
	@Test
	@DirtiesContext
	public void testSpamNo() throws AddressException, MessagingException{
		
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setSender(new InternetAddress("mmarmol@mxhero.com"));
		message.setFrom(new InternetAddress("mmarmol@mxhero.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(
				"mmarmol@mxhero.com"));
		message.setSubject("enlarge your ***");
		message.setText("texto");
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();
		
		MimeMail mail = new MimeMail("mmarmol@mxhero.com", "mmarmol@mxhero.com", message, "service");
		Result result = ctx.getBean(SpamAssassimScan.class).exec(mail);
		Assert.assertFalse(result.isTrue());
		log.info(result.toString());
	}

	@Test
	@DirtiesContext
	public void testSpamYes() throws FileNotFoundException, MessagingException{
		FileInputStream is = new FileInputStream(this.getClass().getClassLoader().getResource("gtube.txt").getFile());
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()),is);
		message.saveChanges();
		
		MimeMail mail = new MimeMail("mmarmol@mxhero.com", "mmarmol@mxhero.com", message, "service");
		Result result = ctx.getBean(SpamAssassimScan.class).exec(mail);
		Assert.assertTrue(result.isTrue());
		log.info(result.toString());
	}
	
	@Test
	@DirtiesContext
	public void testNoHeaders() throws AddressException, MessagingException{
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setSender(new InternetAddress("mmarmol@mxhero.com"));
		message.setFrom(new InternetAddress("mmarmol@mxhero.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(
				"mmarmol@mxhero.com"));
		message.setSubject("enlarge your ****");
		message.setText("texto");
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();
		
		MimeMail mail = new MimeMail("mmarmol@mxhero.com", "mmarmol@mxhero.com", message, "service");
		Result result = ctx.getBean(SpamAssassimScan.class).exec(mail,"false");
		Assert.assertFalse(result.isTrue());
		log.info(result.toString());
	}
	
	@Test
	@DirtiesContext
	public void testChangeHeaders() throws AddressException, MessagingException{
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setSender(new InternetAddress("mmarmol@mxhero.com"));
		message.setFrom(new InternetAddress("mmarmol@mxhero.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(
				"mmarmol@mxhero.com"));
		message.setSubject("enlarge your ***");
		message.setText("texto");
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();
		
		MimeMail mail = new MimeMail("mmarmol@mxhero.com", "mmarmol@mxhero.com", message, "service");
		Result result = ctx.getBean(SpamAssassimScan.class).exec(mail,"true","my-spam-flag","my-spam-status");
		Assert.assertFalse(result.isTrue());
		log.info(result.toString());
	}
}
