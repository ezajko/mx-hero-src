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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.mxhero.engine.plugin.attachmentlink.fileserver.domain.ContentDTO;
import org.mxhero.engine.plugin.attachmentlink.fileserver.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SMTPMailService implements MailService{

	private static Logger log = Logger.getLogger(SMTPMailService.class);
	private static final String SENDER_KEY = "mxsender";
	private static final String RECIPIENT_KEY = "mxrecipient";
	private static final String ATTACHMENT_NAME_KEY = "file-name";
	private static final String ACCESS_DATE_KEY = "attachmentlink-access-date";
	private static final String UNSUBSCRIVE_LINK = "unsubscribe-link";
	
	@Autowired(required=true)
	private SMTPSenderConfig config;
	
	@Autowired
	private PBEStringEncryptor encryptor;
	
	@Override
	public void sendMailToSender(ContentDTO contentDTO) {
		try{
			
			SMTPSender.send(contentDTO.getSubject(), getContent(contentDTO,contentDTO.getMsgMail(),false), getContent(contentDTO,contentDTO.getMsgMailHtml(),true), contentDTO.getSenderMail(), contentDTO.getMessageId(), config);
		}catch(Exception e){
			log.error("error sending message to "+contentDTO.getSenderMail(),e);
		}
	}
	
	private String getContent(ContentDTO contentDTO, String messageContent, boolean isHtml){
		String content = messageContent;
		if(content!=null){
			
			String tagregex = "\\$\\{[^\\{]*\\}";
			Pattern p2 = Pattern.compile(tagregex);
			StringBuffer sb = new StringBuffer();
			Matcher m2 = p2.matcher(content);
			int lastIndex = 0;
			
			while (m2.find()) {
			lastIndex=m2.end();
			  String key =content.substring(m2.start()+2,m2.end()-1);
			  
			  if(key.equalsIgnoreCase(SENDER_KEY)){
				  m2.appendReplacement(sb, contentDTO.getSenderMail());
			  }else if(key.equalsIgnoreCase(RECIPIENT_KEY)){
				  m2.appendReplacement(sb, contentDTO.getRecipientMail());
			  }else if(key.equalsIgnoreCase(ATTACHMENT_NAME_KEY)){
				  m2.appendReplacement(sb, contentDTO.getFileName());
			  }else if(key.equalsIgnoreCase(ACCESS_DATE_KEY)){
				  m2.appendReplacement(sb, Calendar.getInstance().getTime().toString());
			  }else if(key.equalsIgnoreCase(UNSUBSCRIVE_LINK)){
				  try {
						String url = config.getExternalUrl()+"/unsubscribe?id="+ URLEncoder.encode(encryptor.encrypt(contentDTO.getIdMessage().toString()),"ASCII");
						if(!isHtml){
							m2.appendReplacement(sb, url);
						}else{
							m2.appendReplacement(sb, "<a href=\""+url+"\">link</a>");
						}
					} catch (UnsupportedEncodingException e) {
						log.error("Error URL Link for HTML attach. MailId: "+contentDTO.getIdMessage()+" - "+e.getClass().getName()+" - "+e.getMessage());
					}
			  }
			}
			sb.append(content.substring(lastIndex));
			content=sb.toString();
		}
		return content;
	}
	
	
	public SMTPSenderConfig getConfig() {
		return config;
	}

	public void setConfig(SMTPSenderConfig config) {
		this.config = config;
	}

	public PBEStringEncryptor getEncryptor() {
		return encryptor;
	}

	public void setEncryptor(PBEStringEncryptor encryptor) {
		this.encryptor = encryptor;
	}

}
