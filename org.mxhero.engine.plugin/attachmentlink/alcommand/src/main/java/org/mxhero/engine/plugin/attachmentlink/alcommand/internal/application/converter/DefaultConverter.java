package org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.converter;

import java.util.List;

import javax.mail.BodyPart;

import org.apache.log4j.Logger;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.AttachmentGenerator;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.Converter;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.Attach;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.Message;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.MessageAttachRecipient;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DefaultConverter implements Converter {
	
	private static Logger logger = Logger.getLogger(DefaultConverter.class);
	
	@Autowired
	@Qualifier(value = "jdbcRepository")
	private AttachmentRepository repository;
	
	@Autowired
	private AttachmentGenerator generator;
	
	@Value("${base.store.filesystem.path}")
	private String baseStorePath;

	@Override
	public void replaceAttachments(Message mail) {
		List<Attach> attachments = repository.getAttachmentsForEmail(mail);
		removeAndReplaceAttachments(mail,attachments);
	}

	@Override
	public void transform(Message mail) {
		List<Attach> attachments = mail.getAttachments(baseStorePath);
		removeAndReplaceAttachments(mail,attachments);
	}

	private void removeAndReplaceAttachments(Message mail, List<Attach> attachments) {
		for(Attach attach : attachments){
			Attach attRepo = repository.getAttachForChecksum(attach);
			MessageAttachRecipient recipient = new MessageAttachRecipient();
			recipient.setEnableToDownload(true);
			recipient.setMessage(mail);
			recipient.setRecipient(mail.getMail().getRecipient());
			if(attRepo != null){
				recipient.setAttach(attRepo);
			}else{
				recipient.setAttach(attach);
			}
			mail.getMessageAttachRecipient().add(recipient);
		}
		mail = repository.save(mail);
		logger.debug("Message was persisted into DB");
		BodyPart createNewAttach = generator.createNewAttach(mail);
		logger.debug("Attach HTML Created for message");
		mail.attach(createNewAttach);
		mail.removeAllAttachNotHtml(createNewAttach);
		logger.debug("Message old attach was transformed successfuly");
	}

}
