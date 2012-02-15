package org.mxhero.engine.plugin.threadlight.internal.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;

import org.mxhero.engine.plugin.threadlight.internal.repository.ThreadRowFinder;
import org.mxhero.engine.plugin.threadlight.internal.repository.ThreadRowRepository;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRowPk;
import org.mxhero.engine.plugin.threadlight.service.ThreadRowService;

public class DefaultThreadRowService implements ThreadRowService{

	private ThreadRowRepository repository;
	
	private ThreadRowFinder finder;
	
	@Override
	public void follow(ThreadRow threadRow, String follower) {
		if(threadRow!=null && threadRow.getPk()!=null && follower!=null){
			if(repository.find(threadRow.getPk())==null){
				ThreadRow newThreadRow = new ThreadRow();
				newThreadRow.setCreationTime(new Timestamp(System.currentTimeMillis()));
				newThreadRow.setPk(threadRow.getPk());
				newThreadRow.setSubject(threadRow.getSubject());
				repository.saveThread(newThreadRow);
			}
			repository.addFollower(threadRow.getPk(), follower);
		}
	}

	@Override
	public ThreadRow reply(ThreadRowPk pk) {
		ThreadRow replyRow = null;
		if(pk!=null && pk.getMessageId()!=null & pk.getRecipientMail()!=null && pk.getSenderMail()!=null){
			replyRow =  repository.find(pk);
			if(replyRow!=null){
				replyRow.setReplyTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
				repository.saveThread(replyRow);
			}
		}
		return replyRow;
	}

	@Override
	public void unfollow(ThreadRowPk pk, String follower) {
		if(pk!=null && follower!=null){
			repository.removeFollower(pk, follower);
		}
	}

	@Override
	public Collection<ThreadRow> findByParameters(ThreadRow threadRow,
			String follower) {
		Collection<ThreadRow> reponse = null;
		if(threadRow!=null){
			reponse = this.finder.findBySpecs(threadRow, follower);
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

