package org.mxhero.console.backend.infrastructure;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.mxhero.console.backend.vo.ConfigurationVO;

public abstract class MailSender {

	private static final String DEFAULT_PORT = "25";
	
	private static final String DEFAULT_SSL_PORT = "465";
	
	private static final String ERROR_SENDING_MAIL="error.sending.mail";
	
	private static final String MAIL_MISSING_CONFIGURATION="mail.missing.configuration";
	
	public static void send(String subject, String body, String recipient, ConfigurationVO configurationVO ){
		 Properties props = new Properties();
		 props.put("mail.smtp.host", configurationVO.getHost());
		 
		 if(configurationVO==null||
			configurationVO.getHost()==null||
			configurationVO.getHost().trim().length()<1||
			configurationVO.getPort()==null||
			configurationVO.getPort()<1||
			configurationVO.getAdminMail()==null||
			configurationVO.getAdminMail().trim().length()<1){
			 throw new BusinessException(MAIL_MISSING_CONFIGURATION);
		 }
	
		 
		 if(configurationVO.getSsl()!=null && configurationVO.getSsl()){
			 props.put("mail.smtp.ssl.enable","true");
			 if(configurationVO.getPort()==null || configurationVO.getPort()<0){
				 props.put("mail.smtp.port",DEFAULT_SSL_PORT);
			 }else{
				 props.put("mail.smtp.port",configurationVO.getPort().toString());
			 }
		 }else{
			 if(configurationVO.getPort()==null || configurationVO.getPort()<0){
				 props.put("mail.smtp.port",DEFAULT_PORT);
			 }else{
				 props.put("mail.smtp.port",configurationVO.getPort().toString());
			 }
		 }
		 
		 if(configurationVO.getAuth()!=null && configurationVO.getAuth()){
			 props.put("mail.smtp.auth","true");
			 
		 }

        try {
        	Session session = Session.getDefaultInstance(props);

        	MimeMessage message = new MimeMessage(session);
        	message.setSender(new InternetAddress(configurationVO.getAdminMail()));
        	message.setFrom(new InternetAddress(configurationVO.getAdminMail()));
        	message.setText(body);
        	message.setSubject(subject);
        	message.addRecipient(RecipientType.TO, new InternetAddress(recipient));
        	message.saveChanges();
        	
        	Transport transport = session.getTransport("smtp");
        	if(configurationVO.getAuth()){
        		transport.connect(configurationVO.getUser(), configurationVO.getPassword());
        	}else{
        		transport.connect();
        	}
        	transport.sendMessage(message, message.getAllRecipients());
		} catch (Exception e) {
			throw new BusinessException(ERROR_SENDING_MAIL);
		}
	}
	
}
