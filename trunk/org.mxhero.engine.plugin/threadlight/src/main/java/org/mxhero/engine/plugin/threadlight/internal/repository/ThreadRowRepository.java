package org.mxhero.engine.plugin.threadlight.internal.repository;

import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRowPk;

public interface ThreadRowRepository {
	
	ThreadRow find(ThreadRowPk pk);
	
	void saveThread(ThreadRow threadRow);
	
	void addFollower(ThreadRowPk pk, String follower);
	
	void removeFollower(ThreadRowPk pk, String follower);

}
