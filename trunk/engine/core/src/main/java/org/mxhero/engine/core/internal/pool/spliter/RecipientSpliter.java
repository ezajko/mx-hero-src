package org.mxhero.engine.core.internal.pool.spliter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.RulePhase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements Spliter interface. It will create a new mail for each recipient.
 * 
 * @author mmarmol
 * 
 */
public class RecipientSpliter implements Spliter {

	private static Logger log = LoggerFactory.getLogger(RecipientSpliter.class);

	/**
	 * Creates a new mail for each recipient.
	 * @see org.mxhero.engine.core.internal.pool.spliter.Spliter#split(org.mxhero.engine.domain.mail.MimeMail)
	 */
	@Override
	public Collection<MimeMail> split(MimeMail mail) {
		log.debug("spliting mail " + mail);
		ArrayList<MimeMail> mails = new ArrayList<MimeMail>();
		MimeMail splitedMail;
		log.debug("recipients "
				+ Arrays.toString(mail.getRecipients().toArray()));
		for (String recipient : mail.getRecipients()) {
			try {
				MimeMessage newMessage = new MimeMessage(mail.getMessage());
				splitedMail = new MimeMail(mail.getInitialSender(),
						new ArrayList<String>(mail.getRecipients()),
						newMessage, mail.getResponseServiceId());
				log.debug("mail splited " + splitedMail);
			} catch (MessagingException e) {
				log.error("Error spliting mail " + mail, e);
				splitedMail = null;
				continue;
			}
			splitedMail.setRecipient(recipient);
			splitedMail.setPhase(RulePhase.RECEIVE);
			mails.add(splitedMail);
			splitedMail = null;
		}
		return mails;
	}

}
