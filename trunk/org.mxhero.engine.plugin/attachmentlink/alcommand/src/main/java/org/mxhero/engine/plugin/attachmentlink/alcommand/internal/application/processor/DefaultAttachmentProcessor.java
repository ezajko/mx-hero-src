/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.processor;

import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.mxhero.engine.commons.connector.InputService;
import org.mxhero.engine.commons.connector.QueueFullException;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.AttachmentProcessor;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.AttachmentTransformer;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.MailSessionSate;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.Message;
import org.mxhero.engine.plugin.postfixconnector.service.PostFixConnectorOutputService;
import org.mxhero.engine.plugin.storageapi.UserResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

//	@Autowired
	private InputService service;
	
	@Value("${body.sender.cloud.storage}")
	private String bodySenderCloudStorage;
	
	
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
				processCloudStorageSender(message);
			}
		}
		MDC.remove("message");
	}

	private void processCloudStorageSender(Message message) {
		if(message.hasToProcessSender()){
			try {
				MimeMessage messagerep = (MimeMessage)message.getMail().getMessage().reply(false);
				StringBuilder messageText = new StringBuilder();
				messageText.append(bodySenderCloudStorage);
				messageText.append(" ");
				UserResult sender = message.getResultCloudStorageSender();
				messageText.append(sender.getBody());
				messagerep.setText(messageText.toString());
				MimeMail replyMail = new MimeMail(message.getSender(),sender.getEmail(),messagerep.getInputStream(),PostFixConnectorOutputService.class.getName());
				if (service == null) {
					logger.warn("core input service is not online");
					message.getResult().setAnError(true);
					message.getResult().setMessage("core input service is not online");
				}
				try {
					service.addMail(replyMail);
				} catch (QueueFullException e) {
					logger.error("queue is full", e);
					message.getResult().setAnError(true);
					message.getResult().setMessage("queue is full");
				}
			} catch (Exception e1) {
				logger.warn("Could not send email cloudstorage to sender");
				message.getResult().setAnError(true);
				message.getResult().setMessage("Could not send email cloudstorage to sender");
			}
		}
		
	}

	@Override
	public void finishProcessing(Message mail) {
		state.finishProcessing(mail);
	}

}
