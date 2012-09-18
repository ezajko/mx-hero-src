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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class SMTPSenderConfig {
	
	@Autowired
	private MessageSource source;
	
	@Autowired(required=true)
	public SMTPSenderConfig(MessageSource source) {
		this.source = source;
	}

	public String getHost() {
		return source.getMessage("smtp.host", null, null);
	}
	
	public Integer getPort() {
		String port = source.getMessage("smtp.port", null, null);
		try{return Integer.parseInt(port);}
		catch(Exception e){
			return null;
		}
	}
	
	public String getAdminMail() {
		return source.getMessage("smtp.adminMail", null, null);
	}
		
	public Boolean getAuth() {
		return Boolean.parseBoolean(source.getMessage("smtp.auth", null, null));
	}

	public Boolean getSsl() {
		return Boolean.parseBoolean(source.getMessage("smtp.ssl", null, null));
	}

	public String getUser() {
		return source.getMessage("smtp.user", null, null);
	}

	public String getPassword() {
		return source.getMessage("smtp.password", null, null);
	}

	public String getSignature() {
		return source.getMessage("smtp.signature", null, null);
	}

	public String getExternalUrl() {
		return source.getMessage("server.external.url", null, null);
	}

	public String getSignaturePlain() {
		return source.getMessage("smtp.signature.plain", null, null);
	}

}
