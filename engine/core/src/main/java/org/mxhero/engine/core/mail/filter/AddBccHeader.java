package org.mxhero.engine.core.mail.filter;

import java.util.HashSet;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage.RecipientType;

import org.mxhero.engine.domain.mail.MimeMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used to detect recipients that don't have headers and add them like bcc
 * @author mmarmol
 */
public class AddBccHeader implements MailFilter {

	private static Logger log = LoggerFactory.getLogger(AddBccHeader.class);

	/**
	 * Get all recipients from the MimeMail, check for all recipients from the
	 * MimeMessage, if there is a recipient in the MimeMail that is not present
	 * in the MimeMessage then it will add that recipient ass BCC.
	 * 
	 * @see org.mxhero.engine.core.mail.filter.MailFilter#process(org.mxhero.engine.domain.mail.MimeMail)
	 */
	@Override
	public void process(MimeMail mail) {
		Set<String> allRecipients = new HashSet<String>(mail.getRecipients());
		InternetAddress[] allAddresses = null;
		try {
			allAddresses = InternetAddress.parse(InternetAddress.toString(mail.getMessage().getAllRecipients()));
			for(InternetAddress address : allAddresses){
				log.debug("address found:["+address.getAddress()+"]");
				allRecipients.remove(address.getAddress());
			}
			for(String recipient : allRecipients){
				mail.getMessage().addRecipient(RecipientType.BCC, new InternetAddress(recipient));
				log.debug("address bcc added:"+recipient);
			}
			mail.getMessage().saveChanges();
		} catch (MessagingException e) {
			log.warn("error while detecting bcc recipients:",e);
		}
	}

}
