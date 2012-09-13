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
