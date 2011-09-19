package org.mxhero.engine.plugin.postfixconnector.internal.snmp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

import junit.framework.Assert;

import org.junit.Test;
import org.mailster.smtp.api.SessionContext;
import org.mailster.smtp.api.TooMuchDataException;
import org.mailster.smtp.api.listener.MessageListener;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.log.LogMail;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.plugin.postfixconnector.internal.fixer.FixEmails;
import org.mxhero.engine.plugin.postfixconnector.internal.fixer.FixReplyType;
import org.mxhero.engine.plugin.postfixconnector.internal.service.PostfixConnector;
import org.mxhero.engine.plugin.postfixconnector.service.PostFixConnectorOutputService;

public class SMTPZuado {

	/**
	 * @throws MessagingException
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@Test
	public void testZuado() throws MessagingException, IOException{
        SMTPListener smtpListener = null;
        PropertiesService properties = new PostfixConnector();    
        MessageListener messageListener = new MessageListener() {
			
			@Override
			public void deliver(SessionContext arg0, String arg1, String arg2,
					InputStream arg3) throws TooMuchDataException, IOException {
				MimeMail mail = null;
				try {
					mail = new MimeMail(arg1, arg2, arg3,
							PostFixConnectorOutputService.class.getName());
		
					LogMail.saveErrorMail(mail.getMessage(), "pre", ".eml", null);
					mail.getMessage().saveChanges();
				} catch (MessagingException e) {
					Assert.fail();
					e.printStackTrace();
				}
				LogMail.saveErrorMail(mail.getMessage(), "post", ".eml", null);
			}
			
			@Override
			public boolean accept(SessionContext arg0, String arg1, String arg2) {
				// TODO Auto-generated method stub
				return true;
			}
		};

			
        smtpListener = new SMTPListener(messageListener);
		smtpListener.setProperties(properties);
        smtpListener.start();
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		sendEmail("zuado2.rfc");

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
