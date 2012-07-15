package org.mxhero.console.backend.repository;

import org.mxhero.console.backend.infrastructure.pagination.common.PageResult;
import org.mxhero.console.backend.vo.ContractHistoryVO;

public interface ContractHistoryRepository {

	PageResult<ContractHistoryVO> readContractHistory(String senderDomain, String recipient, Integer limit, Integer offset);
	
}
