package org.mxhero.engine.plugin.basecommands.internal.command;

import java.io.IOException;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.command.Result;
import org.mxhero.engine.plugin.basecommands.command.AddText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements AddText interface. Add plain text to an email. Accepts 2
 * parameters. The first parameter is the text, and the second is the position,
 * allowing top or bottom.
 * 
 * @author mmarmol
 */
public class AddTextImpl implements AddText {

	private static Logger log = LoggerFactory.getLogger(AddTextImpl.class);

	private static final int MIM_PARAMANS = 1;
	private static final int MAX_PARAMANS = 3;
	private static final int TEXT_PARAM_NUMBER = 0;
	private static final int POSITION_PARAM_NUMBER = 1;
	private static final int TYPE_CONTENT_NUMBER = 2;

	public static final String TOP_POSITION = "top";
	public static final String BOTTOM_POSITION = "bottom";

	private static final String TEXT_CONTENT = "text";
	private static final String HTML_CONTENT = "html";
	
	public static final String TEXT_TYPE = "text/*";
	public static final String TEXT_PLAIN_TYPE = "text/plain";
	public static final String TEXT_HTML_TYPE = "text/html";
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
		String type = TEXT_CONTENT;
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
		case 3: {
			if(args[TYPE_CONTENT_NUMBER]!=null
					&&(args[TYPE_CONTENT_NUMBER].equalsIgnoreCase(HTML_CONTENT)
						|| args[TYPE_CONTENT_NUMBER].equalsIgnoreCase(TEXT_CONTENT))){
					type = args[TYPE_CONTENT_NUMBER].toLowerCase();
			} else {
				log.warn("wrong type");
				result.setText(WRONG_PARAMS);
				return result;
			}
		}
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
			addText(mail.getMessage(), text, position,type);
			mail.getMessage().saveChanges();
			result.setResult(true);
		} catch (MessagingException e) {
			log.warn("message error for mail " + mail, e);
		} catch (IOException e) {
			log.warn("IO error for mail " + mail, e);
		}

		return result;
	}

	private static void addText(Part p, String text, String position, String type)
			throws MessagingException, IOException {

		if (p.isMimeType(TEXT_TYPE)
				&& (p.getDisposition() == null || (!p.getDisposition().equals(
						Part.INLINE))
						&& !p.getDisposition().equals(Part.ATTACHMENT))) {

			if (position.equals(TOP_POSITION)) {
				if(type.equals(TEXT_CONTENT) && p.isMimeType(TEXT_PLAIN_TYPE)){
					p.setContent(text + ((String) p.getContent()), p
							.getContentType());
				} else if (type.equals(HTML_CONTENT) && p.isMimeType(TEXT_HTML_TYPE)){
					Document doc = Jsoup.parse((String) p.getContent());
					doc.body().prepend(text);
					p.setContent(doc.outerHtml(), p
							.getContentType());
				}
			} else if (position.equals(BOTTOM_POSITION)) {
				if(type.equals(TEXT_CONTENT) && p.isMimeType(TEXT_PLAIN_TYPE)){
					p.setContent(((String) p.getContent()) + text, p
							.getContentType());
				} else if (type.equals(HTML_CONTENT) && p.isMimeType(TEXT_HTML_TYPE)){
					Document doc = Jsoup.parse((String) p.getContent());
					doc.body().append(text);
					p.setContent(doc.outerHtml(), p
							.getContentType());
				}
			}
		} else if (p.isMimeType(MULTIPART_TYPE)) {
			Multipart mp = (Multipart) p.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				addText(mp.getBodyPart(i), text, position, type);
			}
		}
	}
}
