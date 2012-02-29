package org.mxhero.engine.plugin.threadlight.internal.service;

import java.sql.Timestamp;
import java.util.Calendar;

import org.mxhero.engine.plugin.threadlight.internal.ThreadLightConfig;
import org.mxhero.engine.plugin.threadlight.internal.repository.ThreadRowFinder;
import org.mxhero.engine.plugin.threadlight.internal.repository.ThreadRowRepository;
import org.mxhero.engine.plugin.threadlight.pagination.common.PageResult;
import org.mxhero.engine.plugin.threadlight.service.ThreadRowService;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRowFollower;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRowPk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultThreadRowService implements ThreadRowService{

	private static Logger log = LoggerFactory.getLogger(DefaultThreadRowService.class);
	
	private ThreadRowRepository repository;
	private ThreadRowFinder finder;
	private ThreadLightConfig config;
	
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
		log.trace("checking thread light reply "+pk);
		if(pk!=null && pk.getMessageId()!=null & pk.getRecipientMail()!=null && pk.getSenderMail()!=null){
			replyRow =  repository.find(pk);
			log.trace("found from repository "+replyRow);
			if(replyRow!=null){
				replyRow.setReplyTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
				log.trace("saving to repository "+replyRow);
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
	public PageResult<ThreadRow> findByParameters(ThreadRow threadRow,
			String follower, int pageNo, int pageSize) {
		PageResult<ThreadRow> reponse = null;
		if(threadRow!=null){
			reponse = this.finder.findBySpecs(threadRow, follower,pageNo, pageSize);
			log.debug("found "+reponse.getTotalRecordsNumber()+" for "+threadRow);
		}
		return reponse;
	}

	@Override
	public void snooze(ThreadRowPk pk) {
		if(pk!=null){
			ThreadRow row = new ThreadRow();
			row.setPk(pk);
			row.setSnoozeTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			repository.saveThread(row);
		}
	}
	
	@Override
	public Integer watchDays() {
		return config.getSinceInDays();
	}
	
	public ThreadRowRepository getRepository() {
		return repository;
	}

	public void setRepository(ThreadRowRepository repository) {
		this.repository = repository;
	}

	public ThreadLightConfig getConfig() {
		return config;
	}

	public void setConfig(ThreadLightConfig config) {
		this.config = config;
	}

	public ThreadRowFinder getFinder() {
		return finder;
	}

	public void setFinder(ThreadRowFinder finder) {
		this.finder = finder;
	}

}

