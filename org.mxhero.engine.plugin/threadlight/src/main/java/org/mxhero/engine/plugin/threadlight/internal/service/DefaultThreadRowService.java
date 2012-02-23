package org.mxhero.engine.plugin.threadlight.internal.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;

import org.mxhero.engine.plugin.threadlight.internal.repository.ThreadRowFinder;
import org.mxhero.engine.plugin.threadlight.internal.repository.ThreadRowRepository;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRowFollower;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRowPk;
import org.mxhero.engine.plugin.threadlight.service.ThreadRowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultThreadRowService implements ThreadRowService{

	private ThreadRowRepository repository;
	private static Logger log = LoggerFactory.getLogger(DefaultThreadRowService.class);
	private ThreadRowFinder finder;
	
	@Override
	public void follow(ThreadRow threadRow, ThreadRowFollower follower) {
		if(threadRow!=null && threadRow.getPk()!=null && follower!=null){
			if(repository.find(threadRow.getPk())==null){
				ThreadRow newThreadRow = new ThreadRow();
				newThreadRow.setCreationTime(new Timestamp(System.currentTimeMillis()));
				newThreadRow.setPk(threadRow.getPk());
				newThreadRow.setSubject(threadRow.getSubject());
				repository.saveThread(newThreadRow);
			}
			repository.addFollower(threadRow, follower);
			log.debug("follow "+threadRow+" by "+follower);
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
				log.debug("reply "+replyRow);
			}
		}
		return replyRow;
	}

	@Override
	public void unfollow(ThreadRowPk pk, String follower) {
		if(pk!=null && follower!=null){
			repository.removeFollower(repository.find(pk), follower);
			log.debug("unfollow "+repository.find(pk));
		}
	}

	@Override
	public Collection<ThreadRow> findByParameters(ThreadRow threadRow,
			String follower) {
		Collection<ThreadRow> reponse = null;
		if(threadRow!=null){
			reponse = this.finder.findBySpecs(threadRow, follower);
			log.debug("found "+reponse.size()+" for "+threadRow);
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

