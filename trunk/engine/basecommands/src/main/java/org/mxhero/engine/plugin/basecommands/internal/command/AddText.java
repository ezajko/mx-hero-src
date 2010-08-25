package org.mxhero.engine.plugin.basecommands.internal.command;

import java.io.IOException;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.command.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements AddText interface. Add plain text to an email. Accepts 2
 * parameters. The first parameter is the text, and the second is the position,
 * allowing top or bottom.
 * 
 * @author mmarmol
 */
public class AddText implements
		org.mxhero.engine.plugin.basecommands.command.AddText {

	private static Logger log = LoggerFactory.getLogger(AddText.class);

	private static final int MIM_PARAMANS = 1;
	private static final int MAX_PARAMANS = 2;
	private static final int TEXT_PARAM_NUMBER = 0;
	private static final int POSITION_PARAM_NUMBER = 1;

	public static final String TOP_POSITION = "top";
	public static final String BOTTOM_POSITION = "bottom";

	private static final String TEXT_PLAIN_TYPE = "text/plain";
	private static final String MULTIPART_TYPE = "multipart/*";
	private static final String WRONG_PARAMS = "wrong params";

	/**
	 * @see org.mxhero.engine.domain.mail.command.Command#exec(org.mxhero.engine.domain.mail.MimeMail,
	 *      java.lang.String[])
	 */
	@Override
	public Result exec(MimeMail mail, String... args) {
		Result result = new Result();
		String text = null;

		/* default values */
		String position = TOP_POSITION;
		/* default values */

		result.setResult(false);

		/* parameters are 1, or 2 */
		if (args == null || args.length < MIM_PARAMANS
				|| args.length > MAX_PARAMANS) {
			log.warn("wrong amount of params");
			result.setText(WRONG_PARAMS);
			return result;
		}

		/* checking parameters format and settings vars to use them */
		switch (args.length) {
		case 2: {
			if (args[POSITION_PARAM_NUMBER] != null
					&& (args[POSITION_PARAM_NUMBER]
							.equalsIgnoreCase(BOTTOM_POSITION) || args[POSITION_PARAM_NUMBER]
							.equalsIgnoreCase(TOP_POSITION))) {
				position = args[POSITION_PARAM_NUMBER]
						.toLowerCase(Locale.ENGLISH);
			} else {
				log.warn("wrong position");
				result.setText(WRONG_PARAMS);
				return result;
			}
		}
		default: {
			if (args[TEXT_PARAM_NUMBER] != null
					&& args[TEXT_PARAM_NUMBER].length() > 0) {
				text = args[TEXT_PARAM_NUMBER];
			} else {
				log.warn("wrong text");
				result.setText(WRONG_PARAMS);
				return result;
			}
		}
		}

		try {
			addText(mail.getMessage(), text, position);
			mail.getMessage().saveChanges();
			result.setResult(true);
		} catch (MessagingException e) {
			log.warn("message error for mail " + mail, e);
		} catch (IOException e) {
			log.warn("IO error for mail " + mail, e);
		}

		return result;
	}

	private static void addText(Part p, String text, String position)
			throws MessagingException, IOException {

		if (p.isMimeType(TEXT_PLAIN_TYPE)
				&& (p.getDisposition() == null || (!p.getDisposition().equals(
						Part.INLINE))
						&& !p.getDisposition().equals(Part.ATTACHMENT))) {
			log.debug("found part");
			log.debug("adding text " + text);
			log.debug("adding real type is " + p.getContentType());
			log.debug("original text " + ((String) p.getContent()));
			if (position.equals(TOP_POSITION)) {
				log.debug("new text " + text + ((String) p.getContent()));
				p.setContent(text + ((String) p.getContent()), p
						.getContentType());
				log.debug("text set " + ((String) p.getContent()));
				log.debug("adding text top");
			} else if (position.equals(BOTTOM_POSITION)) {
				log.debug("new text " + ((String) p.getContent()) + text);
				p.setContent(((String) p.getContent()) + text, p
						.getContentType());
				log.debug("text set " + ((String) p.getContent()));
				log.debug("adding text bottom");
			}
		} else if (p.isMimeType(MULTIPART_TYPE)) {
			Multipart mp = (Multipart) p.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				addText(mp.getBodyPart(i), text, position);
			}
		}
	}
}
