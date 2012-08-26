package org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.link;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.LinkManager;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.MessageAttachRecipient;
import org.mxhero.engine.plugin.attachmentlink.cloudstorage.client.external.UserResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ShortenedLinkManager implements LinkManager {
	
	private static Logger log = Logger.getLogger(ShortenedLinkManager.class);
	
	@Value("${http.file.server.attach}")
	private String urlFileServer;
	@Autowired
	private PBEStringEncryptor encryptor;
	
	@Override
	public String createLink(MessageAttachRecipient mail) {
		String finalTinyUrl = null;
		StringBuffer fullUrl = new StringBuffer(urlFileServer);
		fullUrl.append("?id=");
		try {
			String id = encryptor.encrypt(mail.getId().toString());
			String encode = URLEncoder.encode(id,"ASCII");
			fullUrl.append(encode);
			appendRecipient(mail, fullUrl);
			finalTinyUrl = fullUrl.toString();
		} catch (UnsupportedEncodingException e) {
			log.error("Error URL Link for HTML attach. MailId: "+mail.getId()+" - "+e.getClass().getName()+" - "+e.getMessage());
			throw new RuntimeException("Could not create Link URL for content "+mail.getMessage().getMessagePlatformId());
		}
		return finalTinyUrl;
	}

	private void appendRecipient(MessageAttachRecipient mail, StringBuffer fullUrl) throws UnsupportedEncodingException {
		UserResult recipient = mail.getMessage().getResultCloudStorageRecipient();
		if(recipient != null){
			String encodeEmail = URLEncoder.encode(recipient.getEmail(),"ASCII");
			fullUrl.append("&amp;email=");
			fullUrl.append(encodeEmail);
		}
	}

}
