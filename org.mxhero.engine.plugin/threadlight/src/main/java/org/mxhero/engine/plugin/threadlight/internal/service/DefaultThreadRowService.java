package org.mxhero.engine.plugin.threadlight.internal.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.plugin.threadlight.internal.repository.ThreadRowRepository;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRow;

public class DefaultThreadRowService implements ThreadRowService{

	private ThreadRowRepository repository;
	private static final String IN_REPLY_HEADER = "In-Reply-To";
	private static final String REFERENCES = "References";
	
	@Override
	public void follow(ThreadRow threadRow) {
		repository.save(threadRow);
	}

	@Override
	public ThreadRow reply(MimeMail mail) {
		//in-reply-to
		ThreadRow replyRow = null;
		try {
			if(mail.getMessage().getHeader(IN_REPLY_HEADER)!=null){
				//when replying the sender of the original thread is now the recipient and the recipient is now the sender 
				replyRow = repository.find(mail.getMessage().getHeader(IN_REPLY_HEADER)[0], mail.getRecipientId(), mail.getSenderId());
			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//also check references
		if(replyRow==null){
			try {
				String refs = null;
				refs = mail.getMessage().getHeader(REFERENCES," ");
				if(refs!=null){
					for(String messageId : MimeUtility.unfold(refs).split(" ")){
						replyRow = repository.find(messageId, mail.getRecipientId(), mail.getSenderId());
						if(replyRow!=null){
							break;
						}
					}
				}
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(replyRow!=null){
			//do magic
		}
		return replyRow;
	}

	public ThreadRowRepository getRepository() {
		return repository;
	}

	public void setRepository(ThreadRowRepository repository) {
		this.repository = repository;
	}

	@Override
	public void unfollow(ThreadRow threadRow, String follower) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<ThreadRow> findByParameters(ThreadRow threadRow,
			String follower) {
		// TODO Auto-generated method stub
		return null;
	}

}

