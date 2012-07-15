package org.mxhero.console.backend.repository;

import org.mxhero.console.backend.infrastructure.pagination.common.PageResult;
import org.mxhero.console.backend.vo.ContractVO;

public interface ContractRepository {
	
	void removeContract(Long id);
	
	PageResult<ContractVO> readContracts(String senderDomain, String recipient, Integer limit, Integer offset);
	
}
