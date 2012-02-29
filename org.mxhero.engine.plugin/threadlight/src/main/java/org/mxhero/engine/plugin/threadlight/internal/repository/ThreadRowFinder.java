package org.mxhero.engine.plugin.threadlight.internal.repository;

import java.sql.Timestamp;
import java.util.Map;

import org.mxhero.engine.plugin.threadlight.pagination.common.PageResult;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRowPk;

public interface ThreadRowFinder {

	PageResult<ThreadRow> findBySpecs(ThreadRow threadRow, String follower, int pageNo, int pageSize);
	
	Map<ThreadRowPk, ThreadRow> findBySpecsMap(Timestamp since);

}
