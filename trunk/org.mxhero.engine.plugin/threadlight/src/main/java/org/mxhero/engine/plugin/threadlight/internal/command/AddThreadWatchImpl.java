package org.mxhero.engine.plugin.threadlight.internal.command;

import java.sql.Timestamp;

import javax.mail.MessagingException;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.plugin.threadlight.command.AddThreadWatch;
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
	public Result exec(MimeMail mail, String... args) {
		Result result = new Result();
		String follower = null;
		String parameters = null;
		//mail can not be null
		if(mail==null || args == null || args.length<1 || args[0] == null || args[0].trim().isEmpty()){
			return result;
		}
		follower=args[0];
		if(args.length>1){
			parameters=args[1];
		}
		//creat thread row
		try{
			ThreadRow threadRow = new ThreadRow();
			threadRow.setPk(new ThreadRowPk(mail.getMessageId(),mail.getSenderId(),mail.getRecipientId()));
			threadRow.setCreationTime(new Timestamp(System.currentTimeMillis()));
			threadRow.setSnoozeTime(threadRow.getCreationTime());
			try {
				threadRow.setSubject(mail.getMessage().getSubject());
			} catch (MessagingException e) {}
			//follow that thread
			ThreadRowFollower threadFollower = new ThreadRowFollower();
			threadFollower.setFollower(follower);
			threadFollower.setFolowerParameters(parameters);
			threadRowService.follow(threadRow,threadFollower);
			log.debug("command follow "+threadRow);
			result.setResult(true);
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