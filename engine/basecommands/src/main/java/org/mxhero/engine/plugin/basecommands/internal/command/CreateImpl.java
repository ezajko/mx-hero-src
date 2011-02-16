package org.mxhero.engine.plugin.basecommands.internal.command;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.mxhero.engine.domain.connector.InputService;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.RulePhase;
import org.mxhero.engine.domain.mail.command.Result;
import org.mxhero.engine.plugin.basecommands.command.Create;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the Create interface. Creates a new blank email. Takes five
 * parameters. First parameter is the sender mail. Second parameter is the
 * recipient mails separated by "," character. Third parameter is the subject.
 * Fourth parameter is the text. And the last parameter is the output service
 * id.
 * 
 * @author mmarmol
 */
public class CreateImpl implements Create {

	private static Logger log = LoggerFactory.getLogger(CreateImpl.class);

	public static final String DIV_CHARACTER = ",";

	private static final int MIM_PARAMS = 5;
	private static final int SENDER_PARAM_NUMBER = 0;
	private static final int RECIPIENTS_PARAM_NUMBER = 1;
	private static final int SUBJECT_PARAM_NUMBER = 2;
	private static final int TEXT_PARAM_NUMBER = 3;
	private static final int OUTSERVICE_PARAM_NUMBER = 4;

	private InputService service;

	/**
	 * @see org.mxhero.engine.domain.mail.command.Command#exec(org.mxhero.engine.domain.mail.MimeMail,
	 *      java.lang.String[])
	 */
	@Override
	public Result exec(MimeMail mail, String... args) {

		Result result = new Result();
		Address sender = null;
		Collection<Address> recipients = new ArrayList<Address>();
		MimeMessage newMessage = null;
		MimeMail newMail = null;

		result.setResult(false);

		if (args == null || args.length < MIM_PARAMS) {
			log.debug("wrong ammount of params.");
			return result;
		} else {
			for (String param : args) {
				if (param == null || param.trim().isEmpty()) {
					log.debug("wrong params.");
					return result;
				}
			}
			try {
				sender = new InternetAddress(args[SENDER_PARAM_NUMBER]);
			} catch (AddressException e) {
				log.warn("sender address is wrong");
				return result;
			}
			for (String recipient : args[RECIPIENTS_PARAM_NUMBER]
					.split(DIV_CHARACTER)) {
				try {
					Address rcptAddress = new InternetAddress(recipient);
					recipients.add(rcptAddress);
				} catch (AddressException e) {
					log.warn("recipient is not valid");
				}
			}
			if (recipients.size() < 1) {
				log.warn("there is no recipients for this mail");
				return result;
			}
			Address[] recipientsArray = new Address[recipients.size()];
			int i = 0;
			for (Address address : recipients) {
				recipientsArray[i] = address;
				i++;
			}
			
			for (Address recipient : recipientsArray){
				try {
					newMessage = new MimeMessage(Session.getDefaultInstance(null));
					newMessage.setSender(sender);
					newMessage.setFrom(sender);
					newMessage.setReplyTo(new Address[] { sender });
					newMessage.addRecipients(RecipientType.TO, recipientsArray);
					newMessage.setSubject(args[SUBJECT_PARAM_NUMBER]);
					newMessage.setText(args[TEXT_PARAM_NUMBER]);
					newMessage.setSentDate(Calendar.getInstance().getTime());
					newMessage.saveChanges();
					newMail = new MimeMail(sender.toString(), recipient.toString(),
							newMessage, args[OUTSERVICE_PARAM_NUMBER]);
					newMail.setPhase(RulePhase.SEND);
					
					if (service == null) {
						log.warn("core input service is not online");
						return result;
					}
					service.addMail(newMail);

				} catch (MessagingException e) {
					log.warn("error while creating new message", e);
					return result;
				}
			}

			result.setResult(true);
		}
		return result;
	}

	/**
	 * @return
	 */
	public InputService getService() {
		return service;
	}

	/**
	 * @param service
	 */
	public void setService(InputService service) {
		this.service = service;
	}

}
