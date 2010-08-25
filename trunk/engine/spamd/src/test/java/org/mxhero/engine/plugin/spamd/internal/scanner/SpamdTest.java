package org.mxhero.engine.plugin.spamd.internal.scanner;

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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/test-bundle-context.xml"})
public class SpamdTest {

	private static Logger log = LoggerFactory.getLogger(SpamdTest.class);

	
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
		
		SpamdScanner scanner= new SpamdScanner("localhost", 783);
		log.info(scanner.toString());
		scanner.scan(message);
		log.info(scanner.toString());
	}
	
	@Test
	@DirtiesContext
	public void testSpamYes() throws AddressException, MessagingException, FileNotFoundException{
		
		FileInputStream is = new FileInputStream(this.getClass().getClassLoader().getResource("gtube.txt").getFile());
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()),is);
		message.saveChanges();
		
		SpamdScanner scanner= new SpamdScanner("localhost", 783);
		log.info(scanner.toString());
		scanner.scan(message);
		log.info(scanner.toString());
	}
}
