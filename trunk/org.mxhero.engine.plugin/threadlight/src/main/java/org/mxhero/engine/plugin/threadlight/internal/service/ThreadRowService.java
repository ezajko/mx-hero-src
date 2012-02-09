package org.mxhero.engine.plugin.threadlight.internal.service;

import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRow;

public interface ThreadRowService {

	void follow(ThreadRow threadRow);
	
	void unfollow(ThreadRow threadRow);
	
	void reply();
}
