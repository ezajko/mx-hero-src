package org.mxhero.engine.plugin.threadlight.internal.command;

import java.sql.Timestamp;

import javax.mail.MessagingException;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.plugin.threadlight.command.AddThreadWatch;
import org.mxhero.engine.plugin.threadlight.internal.service.ThreadRowService;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRow;

public class AddThreadWatchImpl implements AddThreadWatch{

	private ThreadRowService threadRowService;
	
	@Override
	public Result exec(MimeMail mail, String... args) {
		Result result = new Result();
		//mail can not be null
		if(mail==null){
			return result;
		}
		//creat thread row
		ThreadRow threadRow = new ThreadRow();
		threadRow.setMessageId(mail.getMessageId());
		threadRow.setCreationTime(new Timestamp(System.currentTimeMillis()));
		threadRow.setRecipientMail(mail.getRecipientId());
		threadRow.setSenderMail(mail.getSenderId());
		try {
			threadRow.setSubject(mail.getMessage().getSubject());
		} catch (MessagingException e) {}
		//follow that thread
		threadRowService.follow(threadRow);
		result.setResult(true);
		return result;
	}

	public ThreadRowService getThreadRowService() {
		return threadRowService;
	}

	public void setThreadRowService(ThreadRowService threadRowService) {
		this.threadRowService = threadRowService;
	}

}
