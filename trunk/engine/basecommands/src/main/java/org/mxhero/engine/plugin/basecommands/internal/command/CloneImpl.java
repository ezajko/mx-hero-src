package org.mxhero.engine.plugin.basecommands.internal.command;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.mxhero.engine.domain.connector.InputService;
import org.mxhero.engine.domain.mail.business.RulePhase;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.command.Result;
import org.mxhero.engine.plugin.basecommands.command.Clone;
import org.mxhero.engine.plugin.basecommands.command.Replay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the Clone interface. Clones an existing mail. Accepts 4 parameters
 * and the 4th is optional. First parameter is the phase for the email
 * RulePhase.SEND or RulePhase.RECEIVE. Second parameter is the sender mail.
 * Third parameter is the recipient mail. And the fourth and optional parameter
 * is the output service id, if not specified the original output service is
 * used.
 * 
 * @author mmarmol
 */
public class CloneImpl implements Clone {

	private static Logger log = LoggerFactory.getLogger(CloneImpl.class);

	private static final int MIM_PARAMANS = 3;
	private static final int PHASE_PARAM_NUMBER = 0;
	private static final int SENDER_PARAM_NUMBER = 1;
	private static final int RECIPIENT_PARAM_NUMBER = 2;
	private static final int OUTPUTSERVICE_PARAM_NUMBER = 3;

	private InputService service;

	/**
	 * @see org.mxhero.engine.domain.mail.command.Command#exec(org.mxhero.engine.domain.mail.MimeMail,
	 *      java.lang.String[])
	 */
	@Override
	public Result exec(MimeMail mail, String... args) {
		Result result = new Result();
		InternetAddress sender = null;
		InternetAddress recipient = null;
		String outputService = null;
		MimeMail clonedMail = null;
		result.setResult(false);

		if (args == null || args.length < MIM_PARAMANS) {
			log.debug("wrong ammount of params.");
			return result;
		} else if (args[PHASE_PARAM_NUMBER] == null
				|| !(args[PHASE_PARAM_NUMBER].equalsIgnoreCase(RulePhase.SEND) || args[PHASE_PARAM_NUMBER]
						.equalsIgnoreCase(RulePhase.RECEIVE))) {
			log.debug("wrong params.");
			return result;
		} else if (args[SENDER_PARAM_NUMBER] == null
				|| args[RECIPIENT_PARAM_NUMBER] == null
				|| args[SENDER_PARAM_NUMBER].isEmpty()
				|| args[RECIPIENT_PARAM_NUMBER].isEmpty()) {
			log.debug("wrong params.");
			return result;
		} else {
			try {
				sender = new InternetAddress(args[SENDER_PARAM_NUMBER]);
			} catch (AddressException e) {
				log.warn("wrong sender address");
				return result;
			}
			try {
				recipient = new InternetAddress(args[RECIPIENT_PARAM_NUMBER]);
			} catch (AddressException e) {
				log.warn("wrong recipient address");
				return result;
			}

			if (args.length > OUTPUTSERVICE_PARAM_NUMBER
					&& args[OUTPUTSERVICE_PARAM_NUMBER] != null
					&& !args[OUTPUTSERVICE_PARAM_NUMBER].isEmpty()) {
				outputService = args[OUTPUTSERVICE_PARAM_NUMBER];
			} else {
				outputService = mail.getResponseServiceId();
			}

			if(!mail.getProperties().containsKey(Replay.class.getName())){
				try {
					clonedMail = new MimeMail(sender.getAddress(), recipient.getAddress(),
							new MimeMessage(mail.getMessage()), outputService);
					clonedMail.setParentMessageId(mail.getParentMessageId());
					clonedMail.setPhase(args[PHASE_PARAM_NUMBER]);
					if (args[0].equals(RulePhase.RECEIVE)) {
						clonedMail.setRecipient(recipient.getAddress());
					}
					clonedMail.getProperties().putAll(mail.getProperties());
					clonedMail.getProperties().put(Replay.class.getName(), recipient.getAddress());
				} catch (MessagingException e) {
					log.warn("error while creating cloned message");
					return result;
				}
	
				if (service == null) {
					log.warn("core input service is not online");
					return result;
				}
				service.addMail(clonedMail);
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
