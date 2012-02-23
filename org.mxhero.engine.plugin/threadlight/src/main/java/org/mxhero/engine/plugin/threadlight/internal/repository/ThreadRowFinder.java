package org.mxhero.engine.plugin.threadlight.internal.repository;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Set;

import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRowPk;

public interface ThreadRowFinder {

	Set<ThreadRow> findBySpecs(ThreadRow threadRow, String follower);
	
	Map<ThreadRowPk, ThreadRow> findBySpecsMap(Timestamp since);

}
