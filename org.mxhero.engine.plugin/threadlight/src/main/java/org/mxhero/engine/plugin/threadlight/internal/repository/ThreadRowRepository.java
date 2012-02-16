package org.mxhero.engine.plugin.threadlight.internal.repository;

import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRowPk;

public interface ThreadRowRepository {
	
	ThreadRow find(ThreadRowPk pk);
	
	Long saveThread(ThreadRow threadRow);
	
	void addFollower(ThreadRow threadRow, String follower);
	
	void removeFollower(ThreadRow threadRow, String follower);

}
