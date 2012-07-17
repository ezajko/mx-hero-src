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
		return source.getMessage("smtp.signature.plain", null, null);
	}

	public String getSignaturePlain() {
		return source.getMessage("server.external.url", null, null);
	}

}
