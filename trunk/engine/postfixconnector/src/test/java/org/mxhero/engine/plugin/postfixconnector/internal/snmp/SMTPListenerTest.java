package org.mxhero.engine.plugin.postfixconnector.internal.snmp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.ParseException;

import org.junit.Test;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.domain.statistic.LogRecord;
import org.mxhero.engine.domain.statistic.LogStat;
import org.mxhero.engine.plugin.postfixconnector.internal.fixer.FixEmails;
import org.mxhero.engine.plugin.postfixconnector.internal.fixer.FixReplyType;
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
			Thread.sleep(10000000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		smtpListener.stop();
	}
	
	/**
	 * @throws MessagingException
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@Test
	public void testSendWrongEmails() throws MessagingException, IOException{
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
		
		sendEmail("pfxc5300455480221297252.eml");
		sendEmail("pfxc551554049437178847.eml");
		sendEmail("pfxc5647026194074193180.eml");
		sendEmail("pfxc6652043909683704136.eml");
		sendEmail("pfxc7454143381794071206.eml");
		sendEmail("pfxc7628358058273183862.eml");
		sendEmail("email-spam1.rfc");
		sendEmail("pfxc5410204471045189223.eml");
		
	    try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		smtpListener.stop();
	}
	
	
	private void sendEmail(String file) throws FileNotFoundException, AddressException, MessagingException{
		System.setProperty("mail.debug","true");
		System.setProperty("mail.mime.contenttypehandler",
				"org.mxhero.javax.mail.handler.ContentTypeHandler");
		System.setProperty("mail.mime.decodeparameters.strict","false");
  		System.setProperty("mail.mime.decodeparameters", "true");
		System.setProperty("mail.mime.encodeparameters", "true");
		System.setProperty("mail.mime.address.strict", "false");
		System.setProperty("mail.mime.allowencodedmessages", "true");
		System.setProperty("mail.mime.applefilenames", "true");
		System.setProperty("mail.mime.base64.ignoreerrors", "true");
		System.setProperty("mail.mime.decodeparameters", "true");
		System.setProperty("mail.mime.encodefilename", "true");
		System.setProperty("mail.mime.ignoreunknownencoding", "true");
		System.setProperty("mail.mime.multipart.allowempty", "true");
		System.setProperty("mail.mime.parameters.strict", "false");
		System.setProperty("mail.mime.uudecode.ignoreerrors", "true");
		System.setProperty("mail.mime.uudecode.ignoremissingbeginend", "true");
		System.setProperty("mail.mime.windowsfilenames", "true");
		System.out.println(System.getProperty("mail.mime.contenttypehandler"));
		Properties props = new Properties();
	    props.put("mail.smtp.host", "localhost");
	    props.put("mail.smtp.port", "25");
	    props.put("mail.smtp.user", "<>");
	    Session session = Session.getInstance(props);
	    Transport t = session.getTransport("smtp");
	    t.connect();

		FileInputStream is = new FileInputStream(this.getClass().getClassLoader().getResource(file).getFile());
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()),is);
		message.setSender(null);
		
		System.out.println("**************************BEFORE*********************");
		System.out.println("To:"+Arrays.toString(message.getHeader("To")));
		System.out.println("From"+Arrays.toString(message.getHeader("From")));
		System.out.println("Cc:"+Arrays.toString(message.getHeader("Cc")));
		System.out.println("Bcc:"+Arrays.toString(message.getHeader("Bcc")));
		System.out.println("Reply-To:"+Arrays.toString(message.getHeader("Reply-To")));
		System.out.println("ContentType"+Arrays.toString(message.getHeader("Content-Type")));
		
		
		new FixEmails().fixit(message);
		new FixReplyType().fixit(message);
		message.saveChanges();

		System.out.println("**************************AFTER*********************");
		System.out.println("To:"+Arrays.toString(message.getHeader("To")));
		System.out.println("From"+Arrays.toString(message.getHeader("From")));
		System.out.println("Cc:"+Arrays.toString(message.getHeader("Cc")));
		System.out.println("Bcc:"+Arrays.toString(message.getHeader("Bcc")));
		System.out.println("Reply-To:"+Arrays.toString(message.getHeader("Reply-To")));
		System.out.println("ContentType"+Arrays.toString(message.getHeader("Content-Type")));
		
		
		
		t.sendMessage(message, new Address[] { new InternetAddress("mmarmol@mxhero.com") });
	    t.close();
	}
	
}
