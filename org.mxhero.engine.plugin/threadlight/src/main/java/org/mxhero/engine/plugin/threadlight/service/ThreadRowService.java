package org.mxhero.engine.plugin.threadlight.service;

import org.mxhero.engine.plugin.threadlight.pagination.common.PageResult;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRowFollower;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRowPk;

public interface ThreadRowService {

	void follow(ThreadRow threadRow, ThreadRowFollower follower);
	
	ThreadRow reply(ThreadRowPk pk);
	
	void unfollow(ThreadRowPk pk, String follower);
	
	void snooze(ThreadRowPk pk);
	
	PageResult<ThreadRow> findByParameters(ThreadRow threadRow, String follower, int pageNo, int pageSize);
	
	Integer watchDays();
}
