package org.mxhero.engine.plugin.postfixconnector.internal.pool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
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
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.statistic.LogStat;
import org.mxhero.engine.plugin.postfixconnector.internal.service.PostfixConnector;
import org.mxhero.engine.plugin.postfixconnector.queue.OutputQueue;

public class OutputPoolTest {

	@Test
	public void testPool() throws AddressException, MessagingException, IOException{
		OutputPool pool = new OutputPool();
		pool.setProperties(new PostfixConnector());
		pool.setLogStat(new LogStat() {
			
			@Override
			public void log(MimeMail mail, String key, String value) {
				System.out.println("log stat");
			}
		});
		pool.start();
		
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
		MimeMail mail = new MimeMail("mmarmol@mxhero.com", Arrays.asList("xxxx@mxhero.com".split(",")), out.toByteArray(), "service");
		mail.setRecipient("xxxx@mxhero.com");
		OutputQueue.getInstance().add(mail);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Assert.assertFalse(OutputQueue.getInstance().contains(mail));
		
	}
	
}
