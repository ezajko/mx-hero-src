package org.mxhero.webapi.infrastructure.mail;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.mxhero.webapi.vo.SystemPropertyVO;

public abstract class MailSender {

	private static final String DEFAULT_PORT = "25";
	
	private static final String DEFAULT_SSL_PORT = "465";
	
	private static final String ERROR_SENDING_MAIL="error.sending.mail";
	
	private static final String MAIL_MISSING_CONFIGURATION="mail.missing.configuration";
	
	public static void send(String subject, String body, String recipient, Properties properties ){
				 
		 if(properties==null||
			properties.get(SystemPropertyVO.MAIL_SMTP_HOST)==null||
			properties.get(SystemPropertyVO.MAIL_SMTP_HOST).toString().trim().length()<1||
			properties.get(SystemPropertyVO.MAIL_SMTP_PORT)==null||
			Integer.parseInt(properties.get(SystemPropertyVO.MAIL_SMTP_PORT).toString())<1||
			properties.get(SystemPropertyVO.MAIL_ADMIN).toString()==null||
			properties.get(SystemPropertyVO.MAIL_ADMIN).toString().trim().length()<1){
			 throw new IllegalArgumentException(MAIL_MISSING_CONFIGURATION);
		 }
		 
		 Properties props = new Properties();
		 props.put("mail.smtp.host", properties.get(SystemPropertyVO.MAIL_SMTP_HOST).toString());
		 
		 if(properties.get(SystemPropertyVO.MAIL_SMTP_SSL_ENABLE)!=null && Boolean.parseBoolean(properties.get(SystemPropertyVO.MAIL_SMTP_SSL_ENABLE).toString())){
			 props.put("mail.smtp.ssl.enable","true");
			 if(properties.get(SystemPropertyVO.MAIL_SMTP_PORT)==null || Integer.parseInt(properties.get(SystemPropertyVO.MAIL_SMTP_PORT).toString())<0){
				 props.put("mail.smtp.port",DEFAULT_SSL_PORT);
			 }else{
				 props.put("mail.smtp.port",properties.get(SystemPropertyVO.MAIL_SMTP_PORT).toString());
			 }
		 }else{
			 if(properties.get(SystemPropertyVO.MAIL_SMTP_PORT)==null || Integer.parseInt(properties.get(SystemPropertyVO.MAIL_SMTP_PORT).toString())<0){
				 props.put("mail.smtp.port",DEFAULT_PORT);
			 }else{
				 props.put("mail.smtp.port",properties.get(SystemPropertyVO.MAIL_SMTP_PORT).toString());
			 }
		 }
		 
		 if(properties.get(SystemPropertyVO.MAIL_SMTP_AUTH)!=null && Boolean.parseBoolean(properties.get(SystemPropertyVO.MAIL_SMTP_AUTH).toString())){
			 props.put("mail.smtp.auth","true");
		 }

        try {
        	Session session = Session.getDefaultInstance(props);

        	MimeMessage message = new MimeMessage(session);
        	message.setSender(new InternetAddress(properties.get(SystemPropertyVO.MAIL_ADMIN).toString()));
        	message.setFrom(new InternetAddress(properties.get(SystemPropertyVO.MAIL_ADMIN).toString()));
        	message.setText(body);
        	message.setSubject(subject);
        	message.addRecipient(RecipientType.TO, new InternetAddress(recipient));
        	message.saveChanges();
        	
        	Transport transport = session.getTransport("smtp");
        	if(Boolean.parseBoolean(properties.get(SystemPropertyVO.MAIL_SMTP_AUTH).toString())){
        		transport.connect(properties.get(SystemPropertyVO.MAIL_SMTP_USER).toString(), properties.get(SystemPropertyVO.MAIL_SMTP_PASSWORD).toString());
        	}else{
        		transport.connect();
        	}
        	transport.sendMessage(message, message.getAllRecipients());
		} catch (Exception e) {
			throw new RuntimeException(ERROR_SENDING_MAIL,e);
		}
	}
	
}
