package org.mxhero.engine.plugin.attachmentlink.alcommand.internal;

import org.mxhero.engine.commons.connector.InputService;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.command.NamedParameters;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.plugin.attachmentlink.alcommand.ALCommandParameters;
import org.mxhero.engine.plugin.attachmentlink.alcommand.AlCommand;
import org.mxhero.engine.plugin.attachmentlink.alcommand.AlCommandResult;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.AttachmentProcessor;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.Message;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.exception.RequeueingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author mmarmol
 *
 */
public class AlCommandImpl implements AlCommand{
	
	@Autowired
	private AttachmentProcessor processor;
	
	private InputService service;
	
	private static Logger log = LoggerFactory.getLogger(AlCommandImpl.class);
	
	@Value("${evaluate.message.as.attachment}")
	private boolean messageToBeEvaluateAsAttach;

	public Result exec(MimeMail mail, NamedParameters parameters) {
		AlCommandResult result = new AlCommandResult();
		Message message = null;
		try {
			ALCommandParameters alParameters = new ALCommandParameters(parameters);
			message = new Message(mail,alParameters);
			if(message.getMessagePlatformId()==null)throw new Exception("Message has not unique ID");
			message.setMsgToBeEvaluateAsAttach(messageToBeEvaluateAsAttach);
			processor.processMessage(message);
			result = message.getResult();
			
			//TODO
			/*			MimeMessage messagerep = (MimeMessage)mail.getMessage().reply(false);
			messagerep.setText("BODY con URL");
			MimeMail replyMail = new MimeMail("from","to",messagerep.getInputStream(),PostFixConnectorOutputService.class.getName());
			
			if (service == null) {
				log.warn("core input service is not online");
				result.setAnError(true);
				result.setMessage("core input service is not online");
				return result;
			}
			try {
				service.addMail(replyMail);
			} catch (QueueFullException e) {
				log.error("queue is full", e);
				result.setAnError(true);
				result.setMessage("queue is full");
				return result;
			}*/	
		} catch (RequeueingException e) {
			result = message.getResult();
		} catch (Exception e) {
			result.setAnError(true);
			result.setConditionTrue(false);
			result.setMessage(e.getMessage());
			log.error(mail.toString(),e.getMessage());
		}finally{
			if(message != null)processor.finishProcessing(message);
		}
		return result;
	}

	public InputService getService() {
		return service;
	}

	public void setService(InputService service) {
		this.service = service;
	}

}
