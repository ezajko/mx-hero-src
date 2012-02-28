package org.mxhero.engine.plugin.threadlight.internal.repository;

import org.mxhero.engine.plugin.threadlight.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRowFollower;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRowPk;

public interface ThreadRowRepository {
	
	ThreadRow find(ThreadRowPk pk);
	
	Long saveThread(ThreadRow threadRow);
	
	void addFollower(ThreadRow threadRow, ThreadRowFollower follower);
	
	void removeFollower(ThreadRow threadRow, String follower);

}
