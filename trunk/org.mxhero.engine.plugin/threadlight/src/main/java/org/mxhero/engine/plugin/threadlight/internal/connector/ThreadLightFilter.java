package org.mxhero.engine.plugin.threadlight.internal.connector;

import java.util.Arrays;

import javax.mail.MessagingException;

import org.mxhero.engine.commons.connector.InputServiceFilter;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.plugin.threadlight.ThreadLightHeaders;
import org.mxhero.engine.plugin.threadlight.internal.service.ThreadRowService;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadLightFilter implements InputServiceFilter{

	private ThreadRowService threadRowService;
	private static Logger log = LoggerFactory.getLogger(ThreadLightFilter.class);
	
	@Override
	public void dofilter(MimeMail mail) {
		ThreadRow threadRow = threadRowService.reply(mail);
		if(threadRow!=null){
			try {
				mail.getMessage().removeHeader(ThreadLightHeaders.MESSAGE_ID);
				mail.getMessage().removeHeader(ThreadLightHeaders.SENDER);
				mail.getMessage().removeHeader(ThreadLightHeaders.RECIPIENT);
				mail.getMessage().removeHeader(ThreadLightHeaders.FOLLOWERS);
				mail.getMessage().addHeader(ThreadLightHeaders.MESSAGE_ID, threadRow.getMessageId());
				mail.getMessage().addHeader(ThreadLightHeaders.SENDER, threadRow.getSenderMail());
				mail.getMessage().addHeader(ThreadLightHeaders.RECIPIENT, threadRow.getRecipientMail());
				if(threadRow.getFollowers()!=null){
					String followers = Arrays.deepToString(threadRow.getFollowers().toArray()).replaceAll("[", "").replaceAll("]", "");
					mail.getMessage().addHeader(ThreadLightHeaders.FOLLOWERS, followers);
				}
				log.debug("thread reply for "+threadRow.toString());
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
