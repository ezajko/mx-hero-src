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

package org.mxhero.engine.plugin.threadlight.internal.connector;

import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;

import org.mxhero.engine.commons.connector.InputServiceFilter;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.plugin.threadlight.ThreadLightHeaders;
import org.mxhero.engine.plugin.threadlight.service.ThreadRowService;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRowFollower;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRowPk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadLightFilter implements InputServiceFilter{

	private ThreadRowService threadRowService;
	private static Logger log = LoggerFactory.getLogger(ThreadLightFilter.class);
	private static final String IN_REPLY_HEADER = "In-Reply-To";
	private static final String REFERENCES = "References";
	
	@Override
	public void dofilter(MimeMail mail) {
		ThreadRow replyRow = null;

		try {
			//in-reply-to
			if(mail.getMessage().getHeader(IN_REPLY_HEADER)!=null){
				//when replying the sender of the original thread is now the recipient and the recipient is now the sender 
				replyRow = threadRowService.reply(new ThreadRowPk(mail.getMessage().getHeader(IN_REPLY_HEADER)[0], mail.getRecipientId(), mail.getSenderId()));
			//check references field
			}else{
				String refs = null;
				refs = mail.getMessage().getHeader(REFERENCES," ");
				if(refs!=null){
					for(String messageId : MimeUtility.unfold(refs).split(" ")){
						//when replying the sender of the original thread is now the recipient and the recipient is now the sender 
						replyRow = threadRowService.reply(new ThreadRowPk(messageId, mail.getRecipientId(), mail.getSenderId()));
						if(replyRow!=null){
							break;
						}
					}
				}
			}
		} catch (MessagingException e) {
			log.warn("error while checking reply",e);
		}

		if(replyRow!=null){
			try {
				mail.getMessage().removeHeader(ThreadLightHeaders.MESSAGE_ID);
				mail.getMessage().removeHeader(ThreadLightHeaders.SENDER);
				mail.getMessage().removeHeader(ThreadLightHeaders.RECIPIENT);
				mail.getMessage().removeHeader(ThreadLightHeaders.FOLLOWER);
				mail.getMessage().addHeader(ThreadLightHeaders.MESSAGE_ID, replyRow.getPk().getMessageId());
				mail.getMessage().addHeader(ThreadLightHeaders.SENDER, replyRow.getPk().getSenderMail());
				mail.getMessage().addHeader(ThreadLightHeaders.RECIPIENT, replyRow.getPk().getRecipientMail());
				if(replyRow.getFollowers()!=null){
					for(ThreadRowFollower follower : replyRow.getFollowers()){
						mail.getMessage().addHeader(ThreadLightHeaders.FOLLOWER, ThreadLightHeaders.FOLLOWER_ID+"="+follower.getFollower()+";"+ThreadLightHeaders.FOLLOWER_PARAMETERS+"=\""+follower.getFolowerParameters()+"\"");
					}
				}
				mail.getMessage().saveChanges();
				log.debug("thread reply for "+replyRow.toString());
			} catch (MessagingException e) {
				log.debug("error while setting headers",e);
			}
		}
	}

	public ThreadRowService getThreadRowService() {
		return threadRowService;
	}

	public void setThreadRowService(ThreadRowService threadRowService) {
		this.threadRowService = threadRowService;
	}

}
