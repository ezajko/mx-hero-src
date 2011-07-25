package org.mxhero.engine.plugin.postfixconnector.internal.fixer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixReplyType implements Fixer {

	private static Logger log = LoggerFactory.getLogger(FixReplyType.class);
	
	public FixReplyType() {
		log.info("Fixer created");
	}

	@Override
	public void fixit(MimeMessage message) throws MessagingException {
		/*fix content type*/
		String contentType = message.getContentType();
	    if(contentType.contains("reply-type")){
		    String replayType = contentType.substring(contentType.lastIndexOf("reply-type"),contentType.length()).trim();
		    contentType = contentType.substring(0,contentType.lastIndexOf("reply-type")).trim();
		    if(!contentType.substring(contentType.length()-1).equals(";")){
		    	contentType=contentType+";";
		    }
		    contentType=contentType+replayType;
		    message.setHeader("Content-Type", contentType);
	    }
	}

}
