package org.mxhero.engine.plugin.threadlight.service;

import java.util.Collection;

import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRowPk;

public interface ThreadRowService {

	void follow(ThreadRow threadRow, String follower);
	
	ThreadRow reply(ThreadRowPk pk);
	
	void unfollow(ThreadRowPk pk, String follower);
	
	Collection<ThreadRow> findByParameters(ThreadRow threadRow, String follower);
}
