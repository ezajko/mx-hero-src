/**
 * 
 */
package org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.processor;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.AttachmentProcessor;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.AttachmentTransformer;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.MailSessionSate;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author royojp
 *
 */
@Service
public class DefaultAttachmentProcessor implements AttachmentProcessor {
	
	private static Logger logger = Logger.getLogger(DefaultAttachmentProcessor.class);
	
	@Autowired
	private MailSessionSate state;
	
	@Autowired
	@Qualifier(value = "firstAttach")
	private AttachmentTransformer transformer;
	
	/**
	 * 
	 */
	public DefaultAttachmentProcessor() {
	}

	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.attachmentlink.application.AttachmentProcessor#processMessage(org.mxhero.engine.plugin.attachmentlink.domain.Message)
	 */
	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRES_NEW)
	public void processMessage(Message message) {
		MDC.put("message", message);
		logger.debug("Processing message...");
		if(!message.valid()){
			logger.debug("Message is not valid. It has no attachment");
			message.continueProcessing();
		}else{
			boolean beenProcessing = state.isBeenProcessing(message);
			if(beenProcessing){
				logger.debug("Message is been processing by another recipient");
				message.requeueMessage();
			}else{
				state.startProcessing(message);
				logger.debug("Message start processing");
				transformer.transformAttachments(message);
				logger.debug("Message transformed");
				message.successMessage();
				state.finishProcessing(message);
				logger.debug("Message transformation finished");
			}
		}
		MDC.remove("message");
	}

	@Override
	public void finishProcessing(Message mail) {
		state.finishProcessing(mail);
	}

}
