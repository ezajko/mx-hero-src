package org.mxhero.engine.plugin.postfixconnector.internal.fixer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public interface Fixer {

	void fixit(MimeMessage message) throws MessagingException ;
	
}
