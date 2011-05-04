package org.mxhero.engine.plugin.postfixconnector.internal.fixer;


import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixEmails implements Fixer {

	private static final String FROM = "From";
	private static final String TO = "To";
	private static final String CC = "Cc";
	private static final String BCC = "Bcc";
	private static final String REPLY_TO = "Reply-to";

	private static Logger log = LoggerFactory.getLogger(FixEmails.class);
	
	public FixEmails() {
		log.debug("Fixer created");
	}

	@Override
	public void fixit(MimeMessage message) throws MessagingException {
		fixEmailHeaderValue(message, FROM);
		fixEmailHeaderValue(message, TO);
		fixEmailHeaderValue(message, CC);
		fixEmailHeaderValue(message, BCC);
		fixEmailHeaderValue(message, REPLY_TO);
	}

	public void fixEmailHeaderValue(MimeMessage message, String key)
			throws MessagingException {
		String value = null;
		String[] headers = null;
		headers = message.getHeader(key);
		boolean wrongAddress = false;
		// if the header exists and has value
		if (headers != null && headers.length > 0) {
			value = headers[0];
			if (value != null && value.trim().length() > 0) {
				try {
					InternetAddress.parse(value, false);
				} catch (AddressException e) {
					wrongAddress = true;
				}
				if (wrongAddress) {
					log.debug("Fix wrong address for header "+key);
					value = parse(value);
					if(value!=null && value.length()>0){
						log.debug("Fixed header "+key);
						message.setHeader(key, value);
					}
					else{
						message.removeHeader(key);
					}
				}
			}
		}
	}

	public String parse(String value) {
		String newValue = "";
		value = value.replace(';', ',').replace("<<", "<").replace(">>", ">")
				.trim();
		String mail = "";
		String name = "";

		InternetAddress addressFormated = null;
		for (String address : value.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)")) {
			name = "";
			mail = "";
			if (!address.contains("@")) {
				name = name + address;
			} else {
				for (String token : address.split("\\s(?=([^\"]*\"[^\"]*\")*[^\"]*$)")) {
					if (token.contains("@")) {
						mail = token.trim();
					} else {
						name = name +" "+ token;
					}
				}
			}
			if (mail.length() > 2) {
				mail = mail.replace("<", "").replace(">", "");
				name = name.trim();
				if (name.length() > 0) {
					if (name.contains(" ")) {
						name = name.replace("\"", "");
						name = "\"" + name + "\"";
					}

					mail = name + " <" + mail + ">";
				}
				try {
					addressFormated = new InternetAddress(mail, false);
					newValue = newValue+addressFormated.toString()+",";
				} catch (AddressException e) {
					log.debug("addres could not be fixed: "+mail);
				}
			}
		}

		if(newValue!=null && newValue.length()>0){
			newValue = newValue.substring(0,newValue.lastIndexOf(","));
		}
		
		return newValue;
	}

}
