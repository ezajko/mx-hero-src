package org.mxhero.engine.plugin.disclaimercontract.contractserver.repository;

import org.mxhero.engine.plugin.disclaimercontract.contractserver.pagination.PageResult;
import org.mxhero.engine.plugin.disclaimercontract.contractserver.vo.ContractHistoryVO;

public interface ContractHistoryRepository {

	PageResult<ContractHistoryVO> readContractHistory(String senderDomain, String recipient, Integer limit, Integer offset);
	
}
