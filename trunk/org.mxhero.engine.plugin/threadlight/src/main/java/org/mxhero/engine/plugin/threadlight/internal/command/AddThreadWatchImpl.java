package org.mxhero.engine.plugin.threadlight.internal.command;

import java.sql.Timestamp;

import javax.mail.MessagingException;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.plugin.threadlight.command.AddThreadWatch;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRowPk;
import org.mxhero.engine.plugin.threadlight.service.ThreadRowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddThreadWatchImpl implements AddThreadWatch{

	private ThreadRowService threadRowService;
	private static Logger log = LoggerFactory.getLogger(AddThreadWatchImpl.class);
	
	@Override
	public Result exec(MimeMail mail, String... args) {
		Result result = new Result();
		//mail can not be null
		if(mail==null || args == null || args.length<1 || args[0] == null || args[0].trim().isEmpty()){
			return result;
		}
		//creat thread row
		try{
			ThreadRow threadRow = new ThreadRow();
			threadRow.setPk(new ThreadRowPk(mail.getMessageId(),mail.getRecipientId(),mail.getSenderId()));
			threadRow.setCreationTime(new Timestamp(System.currentTimeMillis()));
			try {
				threadRow.setSubject(mail.getMessage().getSubject());
			} catch (MessagingException e) {}
			//follow that thread
			threadRowService.follow(threadRow,args[0]);
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
