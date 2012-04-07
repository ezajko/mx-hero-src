package org.mxhero.engine.plugin.basecommands.internal.command;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.mxhero.engine.commons.connector.InputService;
import org.mxhero.engine.commons.connector.QueueFullException;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.mail.command.NamedParameters;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.plugin.basecommands.command.reply.Reply;
import org.mxhero.engine.plugin.basecommands.command.reply.ReplyParameters;
import org.mxhero.engine.plugin.basecommands.internal.util.SharedTmpFileInputStream;
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
public class ReplyImpl implements Reply {

	private static Logger log = LoggerFactory.getLogger(ReplyImpl.class);

	private static final String TMP_FILE_SUFFIX = ".eml";
	private static final String TMP_FILE_PREFIX = "reply";
	private static final int DEFERRED_SIZE = 1 * 1024 * 1024;

	private static final String SENDER_KEY = "mxsender";
	private static final String RECIPIENT_KEY = "mxrecipient";

	private InputService service;

	private String noReplySignature = "";
	private String noReplyHTLMSignature = "";

	/**
	 * @see org.mxhero.engine.domain.mail.command.Command#exec(org.mxhero.engine.domain.mail.MimeMail,
	 *      java.lang.String[])
	 */
	@Override
	public Result exec(MimeMail mail, NamedParameters parameters) {
		Result result = new Result();
		result.setParameters(parameters);
		InternetAddress sender = null;
		InternetAddress recipient = null;
		String outputService = null;
		MimeMail replayMail = null;
		String plainText = null;
		String htmlText = null;
		Boolean includeMessage = null;
		ReplyParameters replyParameters = new ReplyParameters(parameters);
		if (replyParameters.getSender()==null
				|| replyParameters.getRecipient() == null
				|| replyParameters.getPlainText() == null
				|| replyParameters.getHtmlText() == null) {
			log.warn("wrong ammount of params.");
			result.setAnError(true);
			result.setMessage("wrong ammount of params");
			return result;
		}

		try {
			String senderEmail = replyParameters.getSender();
			sender = new InternetAddress(senderEmail, false);
			String recipientEmail = replyParameters.getRecipient();
			recipient = new InternetAddress(recipientEmail, false);
			outputService = replyParameters.getOutputService();
			if (outputService == null) {
				outputService = mail.getResponseServiceId();
			}
			plainText = replyParameters.getPlainText();
			htmlText = replyParameters.getHtmlText();
			includeMessage = replyParameters.getIncludeMessage();
			if (includeMessage == null) {
				includeMessage = false;
			}
		} catch (Exception e) {
			log.warn("wrong sender address");
			return result;
		}

		if (!mail.getProperties().containsKey(Reply.class.getName())) {
			try {
				MimeMessage replayMessage = (MimeMessage) mail.getMessage()
						.reply(false);
				replayMessage.setSender(sender);
				replayMessage.setFrom(sender);
				replayMessage.setReplyTo(new InternetAddress[] { sender });
				MimeMultipart mixed = new MimeMultipart();
				MimeMultipart multipartText = new MimeMultipart("alternative");

				if (plainText != null && !plainText.isEmpty()) {
					BodyPart textBodyPart = new MimeBodyPart();
					textBodyPart.setText(replaceTextVars(mail, plainText)
							+ ((noReplySignature != null) ? noReplySignature
									: ""));
					multipartText.addBodyPart(textBodyPart);
				}
				if (htmlText != null && !htmlText.isEmpty()) {
					BodyPart htmlBodyPart = new MimeBodyPart();
					Document doc = Jsoup.parse(replaceTextVars(mail, htmlText));
					if ((noReplyHTLMSignature != null)
							&& !noReplyHTLMSignature.trim().isEmpty()) {
						doc.body().append(noReplyHTLMSignature);
					}
					htmlBodyPart.setContent(doc.outerHtml(), "text/html");
					multipartText.addBodyPart(htmlBodyPart);
				}
				MimeBodyPart wrap = new MimeBodyPart();
				wrap.setContent(multipartText);
				mixed.addBodyPart(wrap);

				if (includeMessage) {
					BodyPart messageBodyPart = new MimeBodyPart();
					messageBodyPart.setContent(mail.getMessage(),
							"message/rfc822");
					mixed.addBodyPart(messageBodyPart);
				}

				replayMessage.setContent(mixed);
				replayMessage.saveChanges();

				InputStream is = null;
				OutputStream os = null;
				try {
					if (mail.getInitialSize() > DEFERRED_SIZE) {
						File tmpFile = File.createTempFile(TMP_FILE_PREFIX,
								TMP_FILE_SUFFIX);
						os = new FileOutputStream(tmpFile);
						replayMessage.writeTo(os);
						is = new SharedTmpFileInputStream(tmpFile);

					} else {
						os = new ByteArrayOutputStream();
						replayMessage.writeTo(os);
						is = new ByteArrayInputStream(
								((ByteArrayOutputStream) os).toByteArray());
					}

					replayMail = new MimeMail(sender.getAddress(),
							recipient.getAddress(), is, outputService);
					replayMail.setPhase(Mail.Phase.out);
					replayMail.getProperties().put(Reply.class.getName(),
							recipient.getAddress());

				} catch (Exception e) {
					log.warn("error while creating cloned message: "
							+ e.getMessage());
					result.setAnError(true);
					result.setMessage(e.getMessage());
					return result;
				} finally {
					if (os != null) {
						try {
							os.flush();
							os.close();
						} catch (Exception e) {
						}
					}
					if (is != null) {
						try {
							is.close();
						} catch (Exception e) {
						}
					}
				}
			} catch (Exception e) {
				log.warn("error while creating replay message");
				result.setAnError(true);
				result.setMessage(e.getMessage());
				return result;
			}

			if (service == null) {
				log.warn("core input service is not online");
				return result;
			}
			try {
				service.addMail(replayMail);
			} catch (QueueFullException e) {
				log.error("queue is full", e);
				return result;
			}
		}
		result.setConditionTrue(true);

		return result;
	}

	private String replaceTextVars(MimeMail mail, String content) {
		String tagregex = "\\$\\{[^\\{]*\\}";
		Pattern p2 = Pattern.compile(tagregex);
		StringBuffer sb = new StringBuffer();
		Matcher m2 = p2.matcher(content);
		int lastIndex = 0;

		while (m2.find()) {
			lastIndex = m2.end();
			String key = content.substring(m2.start() + 2, m2.end() - 1);

			if (key.equalsIgnoreCase(SENDER_KEY)) {
				m2.appendReplacement(sb, mail.getSender());
			} else if (key.equalsIgnoreCase(RECIPIENT_KEY)) {
				m2.appendReplacement(sb, mail.getRecipient());
			} else {
				// this should be a header
				try {
					String[] headers = mail.getMessage().getHeader(key);
					if (headers != null) {
						m2.appendReplacement(sb, headers[0]);
					}
				} catch (MessagingException e) {
					// do nothing, just ignore this header
				}
			}

		}
		sb.append(content.substring(lastIndex));
		return sb.toString();
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

	public String getNoReplySignature() {
		return noReplySignature;
	}

	public void setNoReplySignature(String noReplySignature) {
		this.noReplySignature = noReplySignature;
	}

	public String getNoReplyHTLMSignature() {
		return noReplyHTLMSignature;
	}

	public void setNoReplyHTLMSignature(String noReplyHTLMSignature) {
		this.noReplyHTLMSignature = noReplyHTLMSignature;
	}

}
