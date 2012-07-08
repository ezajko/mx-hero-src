package org.mxhero.feature.replytimeout.provider.internal.noreplycheck.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import org.jsoup.Jsoup;
import org.mxhero.engine.commons.connector.InputService;
import org.mxhero.engine.commons.connector.QueueFullException;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRow;
import org.mxhero.feature.replytimeout.provider.internal.Provider;
import org.mxhero.feature.replytimeout.provider.internal.config.ReplyTimeoutConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageSender {

	private static Logger log = LoggerFactory.getLogger(MessageSender.class);

	private static final String RECIPIENT_KEY = "mxrecipient";
	private static final String SUBJECT_KEY = "subject";
	private static final String TIMEOUT_KEY = "timeout";
	private static final String DATE_KEY = "date";
	private static final String DEFAULT_TIMEOUT = "1d";

	private ReplyTimeoutConfig config;
	private InputService service;

	public boolean createAndSend(ThreadRow row) {
		try {
			String senderMail = row.getFollowers().iterator().next()
					.getFolowerParameters().split(";")[2];
			String locale = row.getFollowers().iterator().next()
					.getFolowerParameters().split(";")[1];
			String dateMail = row.getFollowers().iterator().next()
					.getFolowerParameters().split(";")[3];			
			MimeMessage replayMessage = new MimeMessage(
					Session.getInstance(new Properties()));
			replayMessage.setSender(new InternetAddress(senderMail, false));
			replayMessage.setFrom(new InternetAddress(senderMail, false));
			replayMessage
					.setReplyTo(new InternetAddress[] { new InternetAddress(
							senderMail, false) });
			replayMessage.setRecipient(RecipientType.TO, new InternetAddress(
					row.getPk().getSenderMail(), false));
			replayMessage.setSubject(row.getSubject().replaceFirst(Provider.REGEX_REMOVE, ""));
			replayMessage.setHeader("In-Reply-To", row.getPk().getMessageId());
			replayMessage.setHeader("References", row.getPk().getMessageId());

			MimeMultipart mixed = new MimeMultipart();
			MimeMultipart multipartText = new MimeMultipart("alternative");

			BodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText(Jsoup.parse(
					replaceTextVars(row.getSubject(), row.getPk()
							.getRecipientMail(), config.getNoReplyTemplate(locale),dateMail)).text());
			multipartText.addBodyPart(textBodyPart);

			BodyPart htmlBodyPart = new MimeBodyPart();
			htmlBodyPart.setContent(
					replaceTextVars(row.getSubject(), row.getPk()
							.getRecipientMail(), config.getNoReplyTemplate(locale),dateMail), "text/html");
			multipartText.addBodyPart(htmlBodyPart);

			MimeBodyPart wrap = new MimeBodyPart();
			wrap.setContent(multipartText);
			mixed.addBodyPart(wrap);

			replayMessage.setContent(mixed);
			replayMessage.saveChanges();

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			replayMessage.writeTo(bos);
			ByteArrayInputStream bis = new ByteArrayInputStream(
					bos.toByteArray());

			MimeMail replayMail = new MimeMail(senderMail, row.getPk().getSenderMail()
					, bis, config.getOutputService());
			replayMail.setPhase(Mail.Phase.out);

			if (service == null) {
				log.warn("core input service is not online");
				return false;
			}
			try {
				service.addMail(replayMail);
			} catch (QueueFullException e) {
				log.error("queue is full", e);
				return false;
			}

		} catch (MessagingException e) {
			log.warn("error while creating replay message");
			return false;
		} catch (Exception e) {
			log.warn("error while creating replay message");
			return false;
		}

		return true;
	}

	private String replaceTextVars(String subject, String recipient,
			String content, String date) {
		String tagregex = "\\$\\{[^\\{]*\\}";
		Pattern p2 = Pattern.compile(tagregex);
		StringBuffer sb = new StringBuffer();
		Matcher m2 = p2.matcher(content);
		int lastIndex = 0;

		while (m2.find()) {
			lastIndex = m2.end();
			String key = content.substring(m2.start() + 2, m2.end() - 1);
			if (key.equalsIgnoreCase(RECIPIENT_KEY)) {
				m2.appendReplacement(sb, recipient);
			} else if (key.equalsIgnoreCase(SUBJECT_KEY)) {
				m2.appendReplacement(sb, subject.replaceFirst(Provider.REGEX_REMOVE, ""));
			} else if (key.equalsIgnoreCase(DATE_KEY)) {
				m2.appendReplacement(sb, date);
			}else if (key.equalsIgnoreCase(TIMEOUT_KEY)) {
				Matcher matcher = Pattern.compile(Provider.REGEX_STRICT).matcher(subject);
				if(matcher.find()){
					String dateParameters = matcher.group().trim().replaceFirst("(?i)\\[\\s*mxreply\\s*", "").replaceFirst("\\s*\\]", "").trim();
					m2.appendReplacement(sb, dateParameters);
				}else{
					m2.appendReplacement(sb, DEFAULT_TIMEOUT);
				}
			}
		}
		sb.append(content.substring(lastIndex));
		return sb.toString();
	}

	public ReplyTimeoutConfig getConfig() {
		return config;
	}

	public void setConfig(ReplyTimeoutConfig config) {
		this.config = config;
	}

	public InputService getService() {
		return service;
	}

	public void setService(InputService service) {
		this.service = service;
	}

}
