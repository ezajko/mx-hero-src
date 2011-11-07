package org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.transformer;

import org.apache.log4j.Logger;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.AttachmentTransformer;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.Converter;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "alreadyAttach")
public class AlreadyAttachTransformer implements AttachmentTransformer {
	
	private static Logger log = Logger.getLogger(AlreadyAttachTransformer.class);
	
	@Autowired
	private Converter converter;

	@Override
	public void transformAttachments(Message mail) {
		log.debug("Message already transform. Transforming again..");
		converter.replaceAttachments(mail);
	}

}
