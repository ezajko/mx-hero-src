package org.mxhero.engine.plugin.attachmentlink.alcommand.internal;


import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.command.NamedParameters;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.plugin.attachmentlink.alcommand.AlCommand;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.AttachmentProcessor;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.Message;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.exception.RequeueingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class AlCommandImpl implements AlCommand{
	
	@Autowired
	private AttachmentProcessor processor;
	
	private static Logger log = LoggerFactory.getLogger(AlCommandImpl.class);
	
	@Value("${evaluate.message.as.attachment}")
	private boolean messageToBeEvaluateAsAttach;

	public Result exec(MimeMail mail, NamedParameters parameters) {
		Result result = new Result();
		Message message = null;
		try {
			message = new Message(mail,parameters);
			if(message.getMessagePlatformId()==null)throw new Exception("Message has not unique ID");
			message.setMsgToBeEvaluateAsAttach(messageToBeEvaluateAsAttach);
			processor.processMessage(message);
			result = message.getResult();
		} catch (RequeueingException e) {
			result = message.getResult();
		} catch (Exception e) {
			e.printStackTrace();
			result.setAnError(true);
			result.setConditionTrue(false);
			result.setMessage("");
			log.error(mail.toString(),e.getMessage());
		}finally{
			if(message != null)processor.finishProcessing(message);
		}
		return result;
	}

}
