package org.mxhero.engine.plugin.threadlight.internal.service;

import java.util.Collection;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRow;

public interface ThreadRowService {

	void follow(ThreadRow threadRow);
	
	ThreadRow reply(MimeMail mail);
	
	void unfollow(ThreadRow threadRow, String follower);
	
	Collection<ThreadRow> findByParameters(ThreadRow threadRow, String follower);
}
