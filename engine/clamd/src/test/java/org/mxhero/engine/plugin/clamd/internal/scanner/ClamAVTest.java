package org.mxhero.engine.plugin.clamd.internal.scanner;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeMessage.RecipientType;

import junit.framework.Assert;

import org.junit.Test;
import org.mxhero.engine.plugin.clamd.internal.service.Clamd;

public class ClamAVTest {

	@Test
	public void testFound() throws AddressException, MessagingException,
			URISyntaxException {
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
				.getClassLoader().getResource("eicar_com.zip").toURI()));
		mbp2.setDataHandler(new DataHandler(fds2));
		mbp2.setFileName(fds2.getName());
		MimeBodyPart mbp3 = new MimeBodyPart();
		FileDataSource fds3 = new FileDataSource(new File(this.getClass()
				.getClassLoader().getResource("eicar_com.zip").toURI()));
		mbp3.setDataHandler(new DataHandler(fds3));
		mbp3.setFileName(fds3.getName());

		MimeMultipart mp = new MimeMultipart();
		mp.addBodyPart(mbp1);
		mp.addBodyPart(mbp2);
		mp.addBodyPart(mbp3);
		
		message.setContent(mp);
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();
		
		ClamAV scanner = new ClamAV(message);
		scanner.setProperties(new Clamd());
		Assert.assertNotNull(scanner.scan());
	}

	@Test
	public void testOk() throws AddressException, MessagingException {
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setSender(new InternetAddress("mmarmol@mxhero.com"));
		message.setFrom(new InternetAddress("mmarmol@mxhero.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(
				"mmarmol@mxhero.com"));
		message.setSubject("subject");
		message.setText("texto");
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();

		ClamAV scanner = new ClamAV(message);
		scanner.setProperties(new Clamd());
		Assert.assertNotNull(scanner.scan());
	}

}
