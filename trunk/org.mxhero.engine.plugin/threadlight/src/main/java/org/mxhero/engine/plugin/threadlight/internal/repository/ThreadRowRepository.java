package org.mxhero.engine.plugin.threadlight.internal.repository;

import java.util.Collection;

import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRow;

public interface ThreadRowRepository {

	void findAll();
	
	Collection<ThreadRow> find(ThreadRow threadRow);
	
	void saveAll(Collection<ThreadRow> threadRow);
	
	void save(ThreadRow threadRow);
	
	void removeAll(Collection<ThreadRow> threadRow);
	
	void remove(ThreadRow threadRow);
	
	void addFollower(ThreadRow threadRow, String follower);
	
	void removeFollower(ThreadRow threadRow, String follower);

}
