package org.mxhero.engine.plugin.threadlight.internal.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;

import org.mxhero.engine.plugin.threadlight.internal.repository.ThreadRowRepository;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.service.ThreadRowService;

public class DefaultThreadRowService implements ThreadRowService{

	private ThreadRowRepository repository;
	
	@Override
	public void follow(ThreadRow threadRow, String follower) {
		if(threadRow!=null && follower!=null){
			repository.save(threadRow);
			repository.addFollower(threadRow, follower);
		}
	}

	@Override
	public ThreadRow reply(ThreadRow threadRow) {
		ThreadRow replyRow = null;
		if(threadRow!=null && threadRow.getMessageId()!=null & threadRow.getRecipientMail()!=null && threadRow.getSenderMail()!=null){
			Collection<ThreadRow> reponse =  repository.find(threadRow);
			if(reponse!=null && reponse.size()>0){
				replyRow = reponse.iterator().next();
				replyRow.setReplyTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
				repository.save(replyRow);
			}
		}
		return replyRow;
	}

	@Override
	public void unfollow(ThreadRow threadRow, String follower) {
		if(threadRow!=null && follower!=null){
			repository.removeFollower(threadRow, follower);
		}
	}

	@Override
	public Collection<ThreadRow> findByParameters(ThreadRow threadRow,
			String follower) {
		Collection<ThreadRow> reponse = null;
		if(threadRow!=null){
			reponse = this.repository.find(threadRow);
		}
		return reponse;
	}

	public ThreadRowRepository getRepository() {
		return repository;
	}

	public void setRepository(ThreadRowRepository repository) {
		this.repository = repository;
	}
}

