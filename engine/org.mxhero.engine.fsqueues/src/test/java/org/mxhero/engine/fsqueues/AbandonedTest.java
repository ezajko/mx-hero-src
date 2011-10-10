package org.mxhero.engine.fsqueues;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

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

import org.junit.Assert;
import org.junit.Test;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.fsqueues.internal.FSConfig;
import org.mxhero.engine.fsqueues.internal.FSQueueService;

public class AbandonedTest {

	//@Test
	public void test() throws AddressException, InterruptedException, MessagingException, URISyntaxException, IOException {
		FSConfig config = new FSConfig("C:\\temp\\store", "C:\\temp\\tmp");
		FSQueueService srv = new FSQueueService(config);
		for (File tmpFile : config.getTmpPath().listFiles()) {
			tmpFile.delete();
		}
		for (File storeFile : config.getStorePath().listFiles()) {
			storeFile.delete();
		}

		srv.init();

		srv.store("send", new MimeMail("s@example.com", "r@example.com", createMessage(), "srv"),1000,TimeUnit.MILLISECONDS);
		srv.logState();
		Assert.assertNotNull(srv.poll("send", 1000, TimeUnit.MILLISECONDS));
		srv.logState();
		Thread.sleep(11000);
		srv.logState();
		Assert.assertNotNull(srv.poll("send", 1000, TimeUnit.MILLISECONDS));
		srv.logState();
		
		srv.stop();
	}

	private InputStream createMessage() throws AddressException,
			MessagingException, URISyntaxException, IOException {
		MimeMessage message = new MimeMessage(
				Session.getDefaultInstance(new Properties()));
		message.setSender(new InternetAddress("sender@example.com"));
		message.setFrom(new InternetAddress("sender@example.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(
				"recipient@example.com"));
		message.setSubject("subject for you");

		MimeBodyPart mbp1 = new MimeBodyPart();
		mbp1.setText("text goes here");
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
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		message.writeTo(os);
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		return is;
	}
}
