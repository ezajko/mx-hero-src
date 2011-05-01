package org.mxhero.engine.plugin.postfixconnector.internal.snmp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.junit.Test;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.statistic.LogRecord;
import org.mxhero.engine.domain.statistic.LogStat;
import org.mxhero.engine.plugin.postfixconnector.internal.service.PostfixConnector;
import org.mxhero.engine.plugin.postfixconnector.internal.util.LogMail;

public class SMTPMessageListenerTest {

	@Test
	public void testAllParams() throws IOException, AddressException, MessagingException{
		SMTPMessageListener listener = new SMTPMessageListener();
		listener.setLogRecordService(new LogRecord() {
			
			@Override
			public void log(MimeMail mail) {
				System.out.println("loggin record");
				
			}
		});
		listener.setLogStatService(new LogStat() {
			
			@Override
			public void log(MimeMail mail, String key, String value) {
				System.out.println("loggin stat");
				
			}
		});
		listener.setProperties(new PostfixConnector());

		listener.accept(null, null, null);
		
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setSender(new InternetAddress("mmarmol@mxhero.com"));
		message.setFrom(new InternetAddress("mmarmol@mxhero.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(
				"mmarmol@mxhero.com"));
		message.setSubject("subject");
		message.setText("texto");
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		message.writeTo(out);
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		
		listener.deliver(null, "from@mxhero.com", "recipient@mxhero.com", in);
	}
	
	@Test
	public void testNullParams() throws IOException, AddressException, MessagingException{
		SMTPMessageListener listener = new SMTPMessageListener();
		listener.setProperties(new PostfixConnector());

		listener.accept(null, null, null);
		
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setSender(new InternetAddress("mmarmol@mxhero.com"));
		message.setFrom(new InternetAddress("mmarmol@mxhero.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(
				"mmarmol@mxhero.com"));
		message.setSubject("subject");
		message.setText("texto");
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		message.writeTo(out);
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		
		listener.deliver(null, "from@mxhero.com", "recipient@mxhero.com", in);
	}
	
	@Test
	public void testErrorSave() throws FileNotFoundException, MessagingException{
		FileInputStream is = new FileInputStream(this.getClass().getClassLoader().getResource("subetha7736726399057695056.eml").getFile());
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()),is);
		message.saveChanges();
		LogMail.saveErrorMail(message,"pfxc","eml");
	}
	
}
