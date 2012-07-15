package org.mxhero.console.backend.service;

import org.mxhero.console.backend.vo.PageVO;

public interface ContractService {

	PageVO readContractHistory(String senderDomain, String recipient, Integer limit, Integer offset);
	
	void removeContract(Long id);
	
	PageVO readContracts(String senderDomain, String recipient, Integer limit, Integer offset);

	PageVO readRequests(String senderDomain, String recipient, Integer limit, Integer offset);
}
