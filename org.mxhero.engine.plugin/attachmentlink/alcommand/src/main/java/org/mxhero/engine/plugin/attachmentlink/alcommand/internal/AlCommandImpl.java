package org.mxhero.engine.plugin.attachmentlink.alcommand.internal;

import java.util.Map;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.command.NamedParameters;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.plugin.attachmentlink.alcommand.ALCommandParameters;
import org.mxhero.engine.plugin.attachmentlink.alcommand.AlCommand;
import org.mxhero.engine.plugin.attachmentlink.alcommand.AlCommandResult;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.AttachmentProcessor;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.Message;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.exception.RequeueingException;
import org.mxhero.engine.plugin.attachmentlink.cloudstorage.external.CloudStorage;
import org.mxhero.engine.plugin.attachmentlink.cloudstorage.external.UserResulType;
import org.mxhero.engine.plugin.attachmentlink.cloudstorage.external.UserResult;
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
	
	private static Logger log = LoggerFactory.getLogger(AlCommandImpl.class);
	
	@Value("${evaluate.message.as.attachment}")
	private boolean messageToBeEvaluateAsAttach;
	
	@Autowired
	private CloudStorage cloudStorage;

	public Result exec(MimeMail mail, NamedParameters parameters) {
		AlCommandResult result = new AlCommandResult();
		Message message = null;
		try {
			ALCommandParameters alParameters = new ALCommandParameters(parameters);
			message = new Message(mail,alParameters);
			if(message.getMessagePlatformId()==null)throw new Exception("Message has not unique ID");
			message.setMsgToBeEvaluateAsAttach(messageToBeEvaluateAsAttach);
			Map<UserResulType, UserResult> process = cloudStorage.process(alParameters);
			message.setResultCloudStorage(process);
			processor.processMessage(message);
			result = message.getResult();
			
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

}
