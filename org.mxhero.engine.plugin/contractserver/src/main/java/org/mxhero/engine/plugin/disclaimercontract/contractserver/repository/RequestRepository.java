package org.mxhero.engine.plugin.disclaimercontract.contractserver.repository;

import org.mxhero.engine.plugin.disclaimercontract.contractserver.pagination.PageResult;
import org.mxhero.engine.plugin.disclaimercontract.contractserver.vo.RequestVO;

public interface RequestRepository {

	RequestVO readRequest(Long id);
	
	void updateRequest(RequestVO requestVO);
	
	PageResult<RequestVO> readRequests(String senderDomain, String recipient, Integer limit, Integer offset);
}
