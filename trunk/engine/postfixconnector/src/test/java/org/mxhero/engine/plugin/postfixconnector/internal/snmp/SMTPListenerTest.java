package org.mxhero.engine.plugin.postfixconnector.internal.snmp;

import java.util.Calendar;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.junit.Test;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.domain.statistic.LogRecord;
import org.mxhero.engine.domain.statistic.LogStat;
import org.mxhero.engine.plugin.postfixconnector.internal.service.PostfixConnector;
import org.mxhero.engine.plugin.postfixconnector.internal.snmp.SMTPListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SMTPListenerTest {

	private static Logger log = LoggerFactory.getLogger(SMTPListenerTest.class);
	
	@Test
	public void testSMTPListenerStartStop(){
        SMTPListener smtpListener = null;
        PropertiesService properties = new PostfixConnector();    
        SMTPMessageListener messageListener = new SMTPMessageListener();
        messageListener.setProperties(properties);
        messageListener.setLogStatService(new LogStat() {
			
			@Override
			public void log(MimeMail mail, String key, String value) {
				log.info("logging stat:"+key+" value:"+value);
				
			}
		});
        messageListener.setLogRecordService(new LogRecord() {
			
			@Override
			public void log(MimeMail mail) {
				log.info("logging record for mail:"+mail);
				
			}
		});
        smtpListener = new SMTPListener(messageListener);
		smtpListener.setProperties(properties);
        smtpListener.start();
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		smtpListener.stop();
	}
	
	//@Test
	public void testSendEmail() throws MessagingException{
        SMTPListener smtpListener = null;
        PropertiesService properties = new PostfixConnector();    
        SMTPMessageListener messageListener = new SMTPMessageListener();
        messageListener.setProperties(properties);
        messageListener.setLogStatService(new LogStat() {
			
			@Override
			public void log(MimeMail mail, String key, String value) {
				log.info("logging stat:"+key+" value:"+value);
				
			}
		});
        messageListener.setLogRecordService(new LogRecord() {
			
			@Override
			public void log(MimeMail mail) {
				log.info("logging record for mail:"+mail);
				
			}
		});
        smtpListener = new SMTPListener(messageListener);
		smtpListener.setProperties(properties);
        smtpListener.start();
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		Properties props = new Properties();
	    props.put("mail.smtp.host", properties.getValue(PostfixConnector.SMTP_HOST));
	    props.put("mail.smtp.port", properties.getValue(PostfixConnector.SMTP_PORT));
	    Session session = Session.getInstance(props);
	    Transport t = session.getTransport("smtp");
	    t.connect();
		
	    MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setSender(new InternetAddress("xxxx@mxhero.com"));
		message.setFrom(new InternetAddress("xxxx@mxhero.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(
				"yyyy@mxhero.com"));
		message.setSubject("subject");
		message.setText("texto");
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();
		t.sendMessage(message, new Address[] { new InternetAddress("yyyy@mxhero.com") });
	    t.close();
	    
	    try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		smtpListener.stop();
	}
}
