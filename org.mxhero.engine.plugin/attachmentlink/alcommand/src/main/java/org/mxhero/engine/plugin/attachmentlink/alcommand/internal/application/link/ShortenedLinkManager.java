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

package org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.link;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.LinkManager;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.MessageAttachRecipient;
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
			finalTinyUrl = fullUrl.toString();
		} catch (UnsupportedEncodingException e) {
			log.error("Error URL Link for HTML attach. MailId: "+mail.getId()+" - "+e.getClass().getName()+" - "+e.getMessage());
			throw new RuntimeException("Could not create Link URL for content "+mail.getMessage().getMessagePlatformId());
		}
		return finalTinyUrl;
	}

}
