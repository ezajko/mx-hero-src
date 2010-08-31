package org.mxhero.engine.core.mail.filter;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage.RecipientType;

import org.mxhero.engine.domain.mail.MimeMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Removes the BCC recipients
 * 
 * @author mmarmol
 * 
 */
public class RemoveBccHeader implements MailFilter {

	private static Logger log = LoggerFactory.getLogger(RemoveBccHeader.class);

	/**
	 * Just removes the BCC header from the MimeMessage, the recipients from the
	 * MimeMail are not modified.
	 * 
	 * @see org.mxhero.engine.core.mail.filter.MailFilter#process(org.mxhero.engine.domain.mail.MimeMail)
	 */
	@Override
	public void process(MimeMail mail) {
		try {
			mail.getMessage()
					.setRecipients(RecipientType.BCC, (Address[]) null);
			log.debug("bcc header removed");
			mail.getMessage().saveChanges();
		} catch (MessagingException e) {
			log.warn("error while removing bcc recipients header:", e);
		}

	}

}
