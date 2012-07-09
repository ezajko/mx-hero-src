package org.mxhero.engine.plugin.basecommands.internal.command;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.mail.BodyPart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
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
import org.mxhero.engine.commons.mail.command.NamedParameters;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.plugin.basecommands.command.create.Create;
import org.mxhero.engine.plugin.basecommands.command.create.CreateParameters;
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

	private InputService service;


	@Override
	public Result exec(MimeMail mail, NamedParameters parameters) {

		MimeMessage newMessage = null;
		MimeMail newMail = null;
		Result result = new Result();
		result.setParameters(parameters);
		InternetAddress sender = null;
		Collection<InternetAddress> recipients = new ArrayList<InternetAddress>();
		String subject = null;
		String ouputService = null;
		String text = null;
		String textHtml = null;
		CreateParameters createParameters = new CreateParameters(parameters);
		if (createParameters.getSender()==null
			|| createParameters.getRecipients()==null
			|| createParameters.getOutputService()==null
			|| createParameters.getSubject()==null
			|| createParameters.getText()==null) {
			log.warn("wrong ammount of params.");
			result.setAnError(true);
			result.setMessage("wrong ammount of params.");
			return result;
		}

		try {
			String senderEmail = createParameters.getSender();
			sender = new InternetAddress(senderEmail, false);
			String recipientEmail = createParameters.getRecipients();
			for (String recipient : recipientEmail.split(DIV_CHARACTER)) {
				try {
					InternetAddress rcptAddress = new InternetAddress(
							recipient, false);
					recipients.add(rcptAddress);
				} catch (AddressException e) {
					log.warn("recipient is not valid " + recipient);
				}
			}
			ouputService = createParameters.getOutputService();
			subject = createParameters.getSubject();
			text = createParameters.getText();
			textHtml = createParameters.getTextHtml();
		} catch (Exception e) {
			log.warn("wrong parameters");
			result.setAnError(true);
			result.setMessage(e.getMessage());
			return result;
		}

		if (recipients.size() < 1) {
			log.warn("there is no recipients for this mail");
			result.setAnError(true);
			result.setMessage("there is no recipients for this mail");
			return result;
		}
		
		InternetAddress[] recipientsArray = recipients
				.toArray(new InternetAddress[0]);

		for (InternetAddress recipient : recipientsArray) {
			try {
				newMessage = new MimeMessage(Session.getDefaultInstance(null));
				newMessage.setSender(sender);
				newMessage.setFrom(sender);
				newMessage.setReplyTo(new InternetAddress[] { sender });
				newMessage.addRecipients(RecipientType.TO, recipientsArray);
				newMessage.setSubject(subject);
				if(textHtml==null){
					newMessage.setText(text);
				}else{
					MimeMultipart mixed = new MimeMultipart();
					MimeMultipart multipartText = new MimeMultipart("alternative");

					BodyPart textBodyPart = new MimeBodyPart();
					textBodyPart.setText(text);
					multipartText.addBodyPart(textBodyPart);

					BodyPart htmlBodyPart = new MimeBodyPart();
					htmlBodyPart.setContent(Jsoup.parse(textHtml).outerHtml(), "text/html");
					multipartText.addBodyPart(htmlBodyPart);
					MimeBodyPart wrap = new MimeBodyPart();
					wrap.setContent(multipartText);
					mixed.addBodyPart(wrap);
					newMessage.setContent(mixed);
				}
				if(createParameters.getInReplyMessagId()!=null){
					newMessage.setHeader("References", createParameters.getInReplyMessagId());
					newMessage.setHeader("In-Reply-To", createParameters.getInReplyMessagId());
				}
				newMessage.setSentDate(Calendar.getInstance().getTime());
				newMessage.saveChanges();
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				newMessage.writeTo(os);
				ByteArrayInputStream is = new ByteArrayInputStream(
						os.toByteArray());
				newMail = new MimeMail(sender.toString(), recipient.toString(),
						is, ouputService);
				if(createParameters.getPhase()!=null){
					newMail.setPhase(createParameters.getPhase());
				}else{
					newMail.setPhase(Mail.Phase.send);
				}
				
				if (service == null) {
					log.warn("core input service is not online");
					result.setAnError(true);
					result.setMessage("core input service is not online");
					return result;
				}
				try {
					service.addMail(newMail);
				} catch (QueueFullException e) {
					log.error("queue is full", e);
					result.setAnError(true);
					result.setMessage("queue is full");
					return result;
				}

			} catch (Exception e) {
				log.warn("error while creating new message", e);
				return result;
			}
		}
		result.setConditionTrue(true);
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
