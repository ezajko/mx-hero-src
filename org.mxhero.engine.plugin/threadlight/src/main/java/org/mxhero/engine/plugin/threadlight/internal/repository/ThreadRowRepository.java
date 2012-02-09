package org.mxhero.engine.plugin.threadlight.internal.repository;

import java.util.Collection;

import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRow;

public interface ThreadRowRepository {

	void findAll();
	
	ThreadRow find(String messageId, String sender, String recipient);
	
	void saveAll(Collection<ThreadRow> threadRow);
	
	void save(ThreadRow threadRow);
	
	void removeAll(Collection<ThreadRow> threadRow);
	
	void remove(ThreadRow threadRow);

}
