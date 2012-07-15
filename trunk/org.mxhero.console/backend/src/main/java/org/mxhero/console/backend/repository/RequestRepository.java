package org.mxhero.console.backend.repository;

import org.mxhero.console.backend.infrastructure.pagination.common.PageResult;
import org.mxhero.console.backend.vo.RequestVO;

public interface RequestRepository {

	PageResult<RequestVO> readRequests(String senderDomain, String recipient, Integer limit, Integer offset);
	
}
