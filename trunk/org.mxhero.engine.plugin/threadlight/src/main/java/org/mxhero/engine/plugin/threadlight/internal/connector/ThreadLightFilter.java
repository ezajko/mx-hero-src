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
						mail.getMessage().addHeader(ThreadLightHeaders.FOLLOWER, follower.getFollower());
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
