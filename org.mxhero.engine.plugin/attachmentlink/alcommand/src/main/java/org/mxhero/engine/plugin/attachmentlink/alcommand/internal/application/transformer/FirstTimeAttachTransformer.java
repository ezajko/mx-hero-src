package org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.transformer;

import org.apache.log4j.Logger;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.AttachmentTransformer;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.Converter;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.MailSessionSate;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service(value = "firstAttach")
public class FirstTimeAttachTransformer implements AttachmentTransformer {
	
	private static Logger logger = Logger.getLogger(FirstTimeAttachTransformer.class);

	@Autowired
	private MailSessionSate session;
	
	@Autowired
	@Qualifier(value = "alreadyAttach")
	private AttachmentTransformer next;
	
	@Autowired
	private Converter converter;
	
	public FirstTimeAttachTransformer() {
	}

	@Override
	public void transformAttachments(Message mail) {
		if(session.hasBeenProcessed(mail)){
			logger.debug("Message was processed. Getting attachments to transform");
			next.transformAttachments(mail);
		}else{
			logger.debug("First Time processing message. Processing...");
			converter.transform(mail);
		}
	}

}
