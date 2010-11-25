package org.mxhero.engine.plugin.basecommands.internal.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.RulePhase;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.mxhero.engine.domain.mail.command.Result;
import org.mxhero.engine.plugin.basecommands.command.AddRecipient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the AddRecipient Interface. Only mails with phase
 * RulePhase.SEND are allowed. First param is the type of the recipient,
 * allowing to, cc, bcc, none. All the other params passed are the recipient
 * email's that are going to be added.
 * 
 * @author mmarmol
 */
public class AddRecipientImpl implements AddRecipient {

	private static Logger log = LoggerFactory.getLogger(AddRecipientImpl.class);

	private static final int MIM_PARAMANS = 2;
	private static final int RECIPIENT_TYPE_PARAM_NUMBER = 0;
	private static final int RECIPIENT_PARAM_NUMBER = 1;

	public static final String ADD_TO = "to";
	public static final String ADD_CC = "cc";
	public static final String ADD_BCC = "bcc";
	public static final String ADD_NONE = "none";
	private static final String DIV_CHARACTER = ",";

	/**
	 * @see org.mxhero.engine.domain.mail.command.Command#exec(org.mxhero.engine.domain.mail.MimeMail,
	 *      java.lang.String[])
	 */
	@Override
	public Result exec(MimeMail mail, String... args) {
		Result result = new Result();
		result.setResult(false);
		RecipientType type = null;
		if (mail == null) {
			log.warn("mail is null");
			return result;
		} else if (!mail.getPhase().equals(RulePhase.SEND)) {
			log.warn("wrong phase, only RulePhase.SEND allowed.");
			return result;
		} else if (args == null || args.length < MIM_PARAMANS) {
			log.warn("wrong ammount of params.");
			return result;
		} else if (args[RECIPIENT_TYPE_PARAM_NUMBER] == null
				|| args[RECIPIENT_PARAM_NUMBER] == null) {
			log.warn("wrong params.");
			return result;
		} else {
			if (args[RECIPIENT_TYPE_PARAM_NUMBER].equalsIgnoreCase(ADD_TO)) {
				type = RecipientType.TO;
			} else if (args[RECIPIENT_TYPE_PARAM_NUMBER]
					.equalsIgnoreCase(ADD_CC)) {
				type = RecipientType.CC;
			} else if (args[RECIPIENT_TYPE_PARAM_NUMBER]
					.equalsIgnoreCase(ADD_BCC)) {
				type = RecipientType.BCC;
			} else if (args[RECIPIENT_TYPE_PARAM_NUMBER]
					.equalsIgnoreCase(ADD_NONE)) {
				log.warn("adding as none type");
			} else {
				log.warn("wrong params.");
				return result;
			}
			result.setText(args[RECIPIENT_TYPE_PARAM_NUMBER]
					.toUpperCase(Locale.ENGLISH)
					+ ":");
			result.setLongField(0);
			Collection<String> newRecipients = new ArrayList<String>();
			newRecipients.addAll(mail.getRecipients());
			for (int i = 1; i < args.length; i++) {
				try {
					Address address = new InternetAddress(args[i]);
					newRecipients.add(args[i]);
					log.debug("added to recipients " + args[i]);
					if (type != null) {
						mail.getMessage().addRecipient(type, address);
						log.debug("added to "
								+ args[RECIPIENT_TYPE_PARAM_NUMBER] + " "
								+ args[i]);
					}
					result.setResult(true);
					result.setText(result.getText() + args[i] + DIV_CHARACTER);
					result.setLongField(result.getLongField() + 1);
				} catch (AddressException e) {
					log.warn("wrong address won't add the client");
				} catch (MessagingException e) {
					log
							.warn("wrong address won't add the client into the mail by the type asked.");
				}
			}
			try {
				mail.getMessage().saveChanges();
			} catch (MessagingException e) {
				log.warn("error while saving mail", e);
			}
			mail.setRecipients(newRecipients);
		}
		return result;
	}

}
