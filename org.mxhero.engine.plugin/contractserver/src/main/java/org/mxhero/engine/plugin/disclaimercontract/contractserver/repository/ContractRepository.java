package org.mxhero.engine.plugin.disclaimercontract.contractserver.repository;

import org.mxhero.engine.plugin.disclaimercontract.contractserver.pagination.PageResult;
import org.mxhero.engine.plugin.disclaimercontract.contractserver.vo.ContractVO;

public interface ContractRepository {
	
	void removeContract(Long id);
	
	PageResult<ContractVO> readContracts(String senderDomain, String recipient, Integer limit, Integer offset);
	
}
