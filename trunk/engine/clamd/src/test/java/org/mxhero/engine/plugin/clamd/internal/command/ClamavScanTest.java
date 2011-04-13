package org.mxhero.engine.plugin.clamd.internal.command;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeMessage.RecipientType;

import junit.framework.Assert;

import org.junit.Test;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.command.Result;
import org.mxhero.engine.plugin.clamd.internal.service.Clamd;
import org.osgi.service.cm.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClamavScanTest {

	private static Logger log = LoggerFactory.getLogger(ClamavScanTest.class);
	
	@Test
	public void testOk() throws AddressException, MessagingException, URISyntaxException, ConfigurationException{
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

		MimeMail mail = new MimeMail("mmarmol@mxhero.com", "mmarmol@mxhero.com", message, "service");
		SingleClamavScan scan = new SingleClamavScan();
		Clamd clamd = new Clamd();
		Properties clamdProperties = new Properties();
		clamdProperties.put(Clamd.CONNECTION_TIMEOUT, "15");
		clamdProperties.put(Clamd.HOSTNAME, "localhost");
		clamdProperties.put(Clamd.PORT, "6665");
		clamd.updated(clamdProperties);
		scan.setProperties(clamd);
		Result result = scan.exec(mail,"true");
		Assert.assertFalse(result.isTrue());
		log.debug(result.toString());
	}

	@Test
	public void testFound() throws AddressException, MessagingException, URISyntaxException, ConfigurationException{
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
		mbp2.setDisposition(Part.ATTACHMENT);
		
		MimeBodyPart mbp3 = new MimeBodyPart();
		FileDataSource fds3 = new FileDataSource(new File(this.getClass()
				.getClassLoader().getResource("eicar_com.zip").toURI()));
		mbp3.setDataHandler(new DataHandler(fds3));
		mbp3.setFileName(fds3.getName());
		mbp3.setDisposition(Part.ATTACHMENT);
		
		MimeMultipart mp = new MimeMultipart();
		mp.addBodyPart(mbp1);
		mp.addBodyPart(mbp2);
		mp.addBodyPart(mbp3);

		message.setContent(mp);
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();
		
		MimeMail mail = new MimeMail("mmarmol@mxhero.com", "mmarmol@mxhero.com", message, "service");
		SingleClamavScan scan = new SingleClamavScan();
		Clamd clamd = new Clamd();
		Properties clamdProperties = new Properties();
		clamdProperties.put(Clamd.CONNECTION_TIMEOUT, "15");
		clamdProperties.put(Clamd.HOSTNAME, "localhost");
		clamdProperties.put(Clamd.PORT, "6665");
		clamd.updated(clamdProperties);
		scan.setProperties(clamd);
		Result result = scan.exec(mail,"true");
		//cleaned the virus
		Assert.assertFalse(result.isTrue());
		log.debug(result.toString());
	}

	@Test
	public void testFoundAfterFound() throws AddressException, MessagingException, URISyntaxException, ConfigurationException{
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
		mbp2.setDisposition(Part.ATTACHMENT);
		
		MimeBodyPart mbp3 = new MimeBodyPart();
		FileDataSource fds3 = new FileDataSource(new File(this.getClass()
				.getClassLoader().getResource("eicar_com.zip").toURI()));
		mbp3.setDataHandler(new DataHandler(fds3));
		mbp3.setFileName(fds3.getName());
		mbp3.setDisposition(Part.ATTACHMENT);
		
		MimeMultipart mp = new MimeMultipart();
		mp.addBodyPart(mbp1);
		mp.addBodyPart(mbp2);
		mp.addBodyPart(mbp3);

		message.setContent(mp);
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();
		
		MimeMail mail = new MimeMail("mmarmol@mxhero.com","mmarmol@mxhero.com", message, "service");
		SingleClamavScan scan = new SingleClamavScan();
		Clamd clamd = new Clamd();
		Properties clamdProperties = new Properties();
		clamdProperties.put(Clamd.CONNECTION_TIMEOUT, "15");
		clamdProperties.put(Clamd.HOSTNAME, "localhost");
		clamdProperties.put(Clamd.PORT, "6665");
		clamd.updated(clamdProperties);
		scan.setProperties(clamd);
		Result result = scan.exec(mail,"true");
		//cleaned the virus
		Assert.assertFalse(result.isTrue());
		log.debug(result.toString());
		scan = new SingleClamavScan();
		scan.setProperties(clamd);
		result = scan.exec(mail,"true");
		Assert.assertFalse(result.isTrue());
		log.debug(result.toString());
	}

	@Test
	public void testFoundNoRemove() throws AddressException, MessagingException, URISyntaxException, ConfigurationException{
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
		mbp2.setDisposition(Part.ATTACHMENT);
		
		MimeBodyPart mbp3 = new MimeBodyPart();
		FileDataSource fds3 = new FileDataSource(new File(this.getClass()
				.getClassLoader().getResource("eicar_com.zip").toURI()));
		mbp3.setDataHandler(new DataHandler(fds3));
		mbp3.setFileName(fds3.getName());
		mbp3.setDisposition(Part.ATTACHMENT);
		
		MimeMultipart mp = new MimeMultipart();
		mp.addBodyPart(mbp1);
		mp.addBodyPart(mbp2);
		mp.addBodyPart(mbp3);

		message.setContent(mp);
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();
		
		MimeMail mail = new MimeMail("mmarmol@mxhero.com", "mmarmol@mxhero.com", message, "service");
		SingleClamavScan scan = new SingleClamavScan();
		Clamd clamd = new Clamd();
		Properties clamdProperties = new Properties();
		clamdProperties.put(Clamd.CONNECTION_TIMEOUT, "15");
		clamdProperties.put(Clamd.HOSTNAME, "localhost");
		clamdProperties.put(Clamd.PORT, "6665");
		clamd.updated(clamdProperties);
		scan.setProperties(clamd);
		Result result = scan.exec(mail,"false");
		//virus found and not cleaned
		Assert.assertTrue(result.isTrue());
		log.debug(result.toString());
		
	}
}
