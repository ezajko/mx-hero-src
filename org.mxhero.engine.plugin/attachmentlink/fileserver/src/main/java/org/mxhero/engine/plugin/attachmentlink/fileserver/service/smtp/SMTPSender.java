/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
        		message.addHeader("In-Reply-To", messageId);     
        		message.addHeader("References", messageId);     
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
