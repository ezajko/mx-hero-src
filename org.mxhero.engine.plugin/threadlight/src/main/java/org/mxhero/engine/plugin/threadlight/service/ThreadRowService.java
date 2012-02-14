package org.mxhero.engine.plugin.threadlight.service;

import java.util.Collection;

import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRow;

public interface ThreadRowService {

	void follow(ThreadRow threadRow, String follower);
	
	ThreadRow reply(ThreadRow threadRow);
	
	void unfollow(ThreadRow threadRow, String follower);
	
	Collection<ThreadRow> findByParameters(ThreadRow threadRow, String follower);
}
