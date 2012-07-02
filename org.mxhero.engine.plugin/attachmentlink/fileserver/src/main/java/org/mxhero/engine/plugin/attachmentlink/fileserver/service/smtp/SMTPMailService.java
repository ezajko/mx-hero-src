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
			
			SMTPSender.send(contentDTO.getSubject(), getContent(contentDTO,contentDTO.getMsgMail()), getContent(contentDTO,contentDTO.getMsgMailHtml()), contentDTO.getSenderMail(), contentDTO.getMessageId(), config);
		}catch(Exception e){
			log.error("error sending message to "+contentDTO.getSenderMail(),e);
		}
	}
	
	private String getContent(ContentDTO contentDTO, String messageContent){
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
						String id = encryptor.encrypt(contentDTO.getIdMessage().toString());
						m2.appendReplacement(sb, config.getExternalUrl()+"/unsubscribe?id="+ URLEncoder.encode(id,"ASCII"));
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
