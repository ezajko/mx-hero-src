package org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;
import javax.mail.internet.MimeUtility;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.business.MailState;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.exception.RequeueingException;


/**
 * @author royojp
 * @version 1.0
 * @created 22-Aug-2011 12:52:23
 */
public class Message {
	
	private static Logger log = Logger.getLogger(Message.class);

	private Long id;
	private String messagePlatformId;
	private Locale locale;
	private MimeMail mail;
	private Long sizeLimit;
	private boolean processed;
	private Set<MessageAttachRecipient> messageAttachRecipient;
	private Result result;
	private boolean msgToBeEvaluateAsAttach;
	private Boolean processAckDownloadMail;
	private String messageAckDownloadMail;
	private String sender;
	private String subject;

	
	private static final String MULTIPART_TYPE = "multipart/*";
	private static final String MESSAGE_TYPE = "message/rfc822";
	private static final String APPLICATION_TYPE = "application/";
	
	public Message(){
		this.processed = false;
		this.result = new Result();
		this.messageAttachRecipient = new HashSet<MessageAttachRecipient>();
	}
	
	public Message(MimeMail mail, String[] args) throws MessagingException{
		this();
		this.mail = mail;
		this.locale =  new Locale(args[0]);
		this.processAckDownloadMail = Boolean.valueOf(args[1]);
		if(args.length>2)this.messageAckDownloadMail = args[2];
		this.sender = mail.getInitialSender();
		this.messagePlatformId = mail.getMessageId();
		this.subject = mail.getMessage().getSubject();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public MimeMail getMail() {
		return mail;
	}

	public void setMail(MimeMail mail) {
		this.mail = mail;
	}

	public Long getSizeLimit() {
		return sizeLimit;
	}

	public void setSizeLimit(Long sizeLimit) {
		this.sizeLimit = sizeLimit;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public void setMessageAttachRecipient(Set<MessageAttachRecipient> messageAttachRecipient) {
		this.messageAttachRecipient = messageAttachRecipient;
	}
	
	public Set<MessageAttachRecipient> getMessageAttachRecipient() {
		return messageAttachRecipient;
	}

	public void setMessagePlatformId(String messagePlatformId) {
		this.messagePlatformId = messagePlatformId;
	}

	public String getMessagePlatformId() {
		return messagePlatformId;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((messagePlatformId == null) ? 0 : messagePlatformId
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (messagePlatformId == null) {
			if (other.messagePlatformId != null)
				return false;
		} else if (!messagePlatformId.equals(other.messagePlatformId))
			return false;
		return true;
	}

	public boolean valid() {
		return hasAttachments();
	}


	public boolean hasAttachments() {
		boolean hasAttach = false;
		try {
			MimeMessage message = getMail().getMessage();
			hasAttach = hasAttach(message);
		} catch (MessagingException e) {
			log.error("Could not verify if the email has attachments. EmailId: "+getMessagePlatformId()+" - "+e.getClass().getName()+" - "+e.getMessage());
			throw new RuntimeException("Could not read mime message");
		} catch (IOException e) {
			log.error("Could not verify if the email has attachments. EmailId: "+getMessagePlatformId()+" - "+e.getClass().getName()+" - "+e.getMessage());
			throw new RuntimeException("Could not read mime message");
		}
		return hasAttach;
	}

	public Result getResult() {
		return this.result;
	}

	public void requeueMessage() {
		getResult().setResult(false);
		getResult().setText(MailState.REQUEUE);
		mail.setStatus(MailState.REQUEUE);
		throw new RequeueingException();
	}

	public void successMessage() {
		getResult().setResult(true);
		getResult().setText("Attachment Processed");
		int size = getMessageAttachRecipient().size();
		getResult().setLongField(size);
		getResult().setDoubleField(Integer.valueOf(size).doubleValue());
	}

	public void continueProcessing() {
		if(!hasAttachments()){
			getResult().setResult(false);
			getResult().setText("The mail has no attachments");
		}
	}

	public List<Attach> getAttachments(String baseStorePath) {
		List<Attach> attachments = new ArrayList<Attach>();
		MimeMessage message = getMail().getMessage();
		try {
			buildAttachments(attachments, message, baseStorePath);
		} catch (Exception e) {
			log.error("Could not obtain the attachment for the email. EmailId: "+getMessagePlatformId()+" - "+e.getClass().getName()+" - "+e.getMessage());
			throw new RuntimeException("Could not obtain attach from email "+getMessagePlatformId());
		}
		return attachments;
	}

	public void attach(BodyPart createNewAttach) {
		try {
			Object content = getMail().getMessage().getContent();
			if(content instanceof Multipart){
				Multipart multi = (Multipart) content;
				multi.addBodyPart(createNewAttach);
			}else{
				Multipart multiPart = new MimeMultipart();
				multiPart.addBodyPart(createNewAttach);
				getMail().getMessage().setContent(multiPart);
			}
			getMail().getMessage().saveChanges();
		} catch (Exception e) {
			log.error("Could not attach html for the email. EmailId: "+getMessagePlatformId()+" - "+e.getClass().getName()+" - "+e.getMessage());
			throw new RuntimeException("Could not attach html for email "+getMessagePlatformId());
		}
	}

	public List<String> getMd5ForAttachs() {
		List<String> checksum = new ArrayList<String>();
		MimeMessage message = getMail().getMessage();
		try {
			buildMd5CheckSums(checksum, message);
		} catch (Exception e) {
			log.error("Could not obtain md5 for attachments. EmailId: "+getMessagePlatformId()+" - "+e.getClass().getName()+" - "+e.getMessage());
			throw new RuntimeException("Could not obtain md5 for attachs from email "+getMessagePlatformId());
		}
		return checksum;
	}

	public Set<MessageAttachRecipient> getMessageAttachRecipientForRecipient() {
		Set<MessageAttachRecipient> messages = new HashSet<MessageAttachRecipient>();
		for(MessageAttachRecipient m : getMessageAttachRecipient()){
			if(m.getRecipient().equals(getMail().getRecipient())){
				messages.add(m);
			}
		}
		return messages;
	}
	
	public void removeAllAttachNotHtml(BodyPart notDelete) {
		try {
			removeAll(getMail().getMessage(), notDelete);
			getMail().getMessage().saveChanges();
		} catch (Exception e) {
			log.error("Could not remove all attachments for the email. EmailId: "+getMessagePlatformId()+" - "+e.getClass().getName()+" - "+e.getMessage());
			throw new RuntimeException("Could not delete all attachments in mail");
		}
	}
	
	public void removeAll(Part part, BodyPart notDelete) throws MessagingException, IOException{
		if(isAttach(part)){
			if(part != notDelete){
				BodyPart multi = (BodyPart) part;
				Multipart parent = multi.getParent();
				parent.removeBodyPart(multi);
			}
		}else if (part.isMimeType(MULTIPART_TYPE)) {
			Multipart mp = (Multipart) part.getContent();
			List<BodyPart> toRemove = new ArrayList<BodyPart>();
			for (int i = 0; i < mp.getCount(); i++) {
				BodyPart bodyPart = mp.getBodyPart(i);
				if(removePart(bodyPart,notDelete)){
					toRemove.add(bodyPart);
				}else{
					removeAll(bodyPart, notDelete);
				}
			}
			for(BodyPart bp : toRemove)mp.removeBodyPart(bp);
		}
	}

	private boolean removePart(BodyPart bodyPart, BodyPart notDelete) throws IOException, MessagingException {
		return isAttach(bodyPart) && (bodyPart!=notDelete);
	}

	public void buildMd5CheckSums(List<String> checksums,Part part) throws MessagingException, IOException {
		if (part.isMimeType(MULTIPART_TYPE)) {
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				buildMd5CheckSums(checksums,mp.getBodyPart(i));
			}
		} else if (isAttach(part)){
			checksums.add(getCheckSum(part));
		}
	}
	
	private String getCheckSum(Part part) throws IOException, MessagingException {
		Attach attach = new Attach();
		attach.buildMd5Checksum(part.getInputStream());
		return attach.getMd5Checksum();
	}

	public void buildAttachments(List<Attach> attachs,Part part, String baseStorePath) throws MessagingException, IOException {
		if (part.isMimeType(MULTIPART_TYPE)) {
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				buildAttachments(attachs,mp.getBodyPart(i),baseStorePath);
			}
		} else if (isAttach(part)){
			attachs.add(getAttach(part,baseStorePath));
		}
	}
	
	private boolean isAttach(Part part) throws MessagingException {
		if(isPureAttach(part)){
			return hasNotContentID(part) && !isAttachInline(part);
		}else{
			return false;
		}
	}

	private boolean isPureAttach(Part part) throws MessagingException {
		boolean isMessageAttach = isMailAttached(part);
		boolean isRealAttach = hasFileName(part) || isAttachAndNotInline(part) || isApplicationType(part);
		return isMessageAttach || isRealAttach;
	}

	private boolean isMailAttached(Part part) throws MessagingException {
		if(isMsgToBeEvaluateAsAttach()){
			return part.isMimeType(MESSAGE_TYPE);
		}
		return false;
	}

	public boolean isMsgToBeEvaluateAsAttach() {
		return msgToBeEvaluateAsAttach;
	}

	public void setMsgToBeEvaluateAsAttach(boolean msgToBeEvaluateAsAttach) {
		this.msgToBeEvaluateAsAttach = msgToBeEvaluateAsAttach;
	}

	private boolean isApplicationType(Part part) throws MessagingException {
		return (part.getContentType() != null && part.getContentType().trim().startsWith(APPLICATION_TYPE));
	}

	private boolean isAttachAndNotInline(Part part) throws MessagingException {
		return (part.getDisposition() != null && part.getDisposition().equals(Part.ATTACHMENT));
	}

	private boolean isAttachInline(Part part) throws MessagingException{
		return part.getDisposition().equals(Part.INLINE);
	}
	
	private boolean hasNotContentID(Part part) throws MessagingException {
		if(part instanceof MimePart){
			MimePart mime = (MimePart) part;
			return StringUtils.isEmpty(mime.getContentID());
		}else{
			return false;
		}
	}

	private boolean hasFileName(Part part) throws MessagingException {
		return (part.getFileName() != null && !part.getFileName().trim().isEmpty());
	}

	public Attach getAttach(Part part, String baseStorePath) throws MessagingException, IOException {
		MimeBodyPart mimePart = (MimeBodyPart) part;
		Attach attach = new Attach();
		if(StringUtils.isEmpty(mimePart.getFileName())){
			attach.setFileName("UNKNOWN");
		}else{
			String fileName = mimePart.getFileName();
			String encoded = System.getProperty("mail.mime.encodefilename");
			if(Boolean.parseBoolean(encoded)){
				fileName = MimeUtility.decodeText(fileName);
			}
			attach.setFileName(fileName);
		}
		ContentType type = new ContentType(mimePart.getContentType());
		attach.setMimeType(type.getBaseType());
		InputStream inputStream = mimePart.getDataHandler().getInputStream();
		attach.buildMd5Checksum(inputStream);
		attach.buildPath(mimePart,baseStorePath);
		return attach;
	}

	public boolean hasAttach(Part part) throws MessagingException, IOException {
		if (part.isMimeType(MULTIPART_TYPE)) {
			Multipart mp = (Multipart) part.getContent();
			boolean has = false;
			for (int i = 0; i < mp.getCount(); i++) {
				if(hasAttach(mp.getBodyPart(i))){
					has = true;
					break;
				}
			}
			return has;
		} else if (isAttach(part)) {
			return true;
		}else{
			return false;
		}
	}

	public void setProcessAckDownloadMail(Boolean processAckDownloadMail) {
		this.processAckDownloadMail = processAckDownloadMail;
	}

	public Boolean getProcessAckDownloadMail() {
		return processAckDownloadMail;
	}

	public void setMessageAckDownloadMail(String messageAckDownloadMail) {
		this.messageAckDownloadMail = messageAckDownloadMail;
	}

	public String getMessageAckDownloadMail() {
		return messageAckDownloadMail;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSender() {
		return sender;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubject() {
		return subject;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Message Id: ");
		builder.append(getMessagePlatformId());
		builder.append(" - ");
		builder.append("Locale: ");
		builder.append(getLocale());
		return builder.toString();
	}

	
	
}