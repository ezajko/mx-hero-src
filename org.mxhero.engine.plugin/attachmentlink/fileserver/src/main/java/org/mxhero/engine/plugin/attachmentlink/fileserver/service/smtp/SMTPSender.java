package org.mxhero.engine.plugin.attachmentlink.fileserver.service.smtp;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class SMTPSender {

	private static final String DEFAULT_PORT = "25";
	
	private static final String DEFAULT_SSL_PORT = "465";
	
	
	public static void send(String subject, String body, String bodyHtml, String recipient, String messageId, SMTPSenderConfig configurationVO ){
		 Properties props = new Properties();
		 props.put("mail.smtp.host", configurationVO.getHost());
		 
		 if(configurationVO==null||
			configurationVO.getHost()==null||
			configurationVO.getHost().trim().length()<1||
			configurationVO.getPort()==null||
			configurationVO.getPort()<1||
			configurationVO.getAdminMail()==null||
			configurationVO.getAdminMail().trim().length()<1){
			 return;
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
			MimeMultipart mixed = new MimeMultipart();
			MimeMultipart multipartText = new MimeMultipart("alternative");

			if (body != null && !body.isEmpty()) {
				BodyPart textBodyPart = new MimeBodyPart();
				textBodyPart.setText(body
						+ ((configurationVO.getSignaturePlain() != null) ? configurationVO.getSignaturePlain()
								: ""));
				multipartText.addBodyPart(textBodyPart);
			}
			if (bodyHtml != null && !bodyHtml.isEmpty()) {
				BodyPart htmlBodyPart = new MimeBodyPart();
				Document doc = Jsoup.parse(bodyHtml);
				if ((configurationVO.getSignature() != null)
						&& !configurationVO.getSignature().trim().isEmpty()) {
					doc.body().append(configurationVO.getSignature());
				}
				htmlBodyPart.setContent(doc.outerHtml(), "text/html");
				multipartText.addBodyPart(htmlBodyPart);
			}
			MimeBodyPart wrap = new MimeBodyPart();
			wrap.setContent(multipartText);
			mixed.addBodyPart(wrap);
			message.setContent(mixed);
			
        	if(subject!=null){
        		message.setSubject(subject);
        	}else{
        		message.setSubject("ATTACHMENT LINK");
        	}
        	if(messageId!=null){
        		message.removeHeader("In-Reply-To");
        		message.addHeader("In-Reply-To", messageId);
        		String newSubject = ""+message.getSubject();
        		if(!newSubject.startsWith("Re:")){
        			newSubject="Re:"+newSubject;
        		}
        		message.setSubject(newSubject);
        	}
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
			throw new RuntimeException(e);
		}
	}
	
}
