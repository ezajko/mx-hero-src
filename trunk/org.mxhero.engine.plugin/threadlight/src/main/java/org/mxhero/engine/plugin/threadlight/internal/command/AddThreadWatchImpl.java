package org.mxhero.engine.plugin.threadlight.internal.command;

import java.sql.Timestamp;

import javax.mail.MessagingException;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.command.NamedParameters;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.plugin.threadlight.command.AddThreadWatch;
import org.mxhero.engine.plugin.threadlight.command.AddThreadWatchParameters;
import org.mxhero.engine.plugin.threadlight.service.ThreadRowService;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRowFollower;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRowPk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddThreadWatchImpl implements AddThreadWatch{

	private ThreadRowService threadRowService;
	private static Logger log = LoggerFactory.getLogger(AddThreadWatchImpl.class);
	
	@Override
	public Result exec(MimeMail mail, NamedParameters parameters) {
		Result result = new Result();
		//mail can not be null
		AddThreadWatchParameters atwParameters = new AddThreadWatchParameters(parameters);
		if(atwParameters.getFollower()==null 
				|| atwParameters.getFollower().trim().isEmpty()){
			log.debug("wrong parameters");
			result.setAnError(true);
			result.setMessage("wrong parameters");
			return result;
		}
		//creat thread row
		try{
			String senderId=mail.getSenderId();
			String recipientId=mail.getRecipientId();
			
			if(atwParameters.getSenderId()!=null && !atwParameters.getSenderId().isEmpty()){
				senderId=atwParameters.getSenderId();
			}
			if(atwParameters.getRecipientId()!=null && !atwParameters.getRecipientId().isEmpty()){
				recipientId=atwParameters.getRecipientId();
			}
			ThreadRow threadRow = new ThreadRow();
			threadRow.setPk(new ThreadRowPk(mail.getMessageId(),senderId,recipientId));
			threadRow.setCreationTime(new Timestamp(System.currentTimeMillis()));
			threadRow.setSnoozeTime(threadRow.getCreationTime());
			try {
				threadRow.setSubject(mail.getMessage().getSubject());
			} catch (MessagingException e) {}
			//follow that thread
			ThreadRowFollower threadFollower = new ThreadRowFollower();
			threadFollower.setFollower(atwParameters.getFollower());
			threadFollower.setFolowerParameters(atwParameters.getParameters());
			threadRowService.follow(threadRow,threadFollower);
			log.debug("command follow "+threadRow);
			result.setConditionTrue(true);
		}catch(Exception e){
			log.error("error while executing follow command",e);
		}
		return result;
	}

	public ThreadRowService getThreadRowService() {
		return threadRowService;
	}

	public void setThreadRowService(ThreadRowService threadRowService) {
		this.threadRowService = threadRowService;
	}

}
