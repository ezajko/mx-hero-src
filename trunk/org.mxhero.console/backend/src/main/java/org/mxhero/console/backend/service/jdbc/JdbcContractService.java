package org.mxhero.console.backend.service.jdbc;

import org.mxhero.console.backend.repository.ContractHistoryRepository;
import org.mxhero.console.backend.repository.ContractRepository;
import org.mxhero.console.backend.repository.RequestRepository;
import org.mxhero.console.backend.service.ContractService;
import org.mxhero.console.backend.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("jdbcContractService")
public class JdbcContractService implements ContractService{

	private ContractRepository contractRepository;
	private RequestRepository requestRepository;
	private ContractHistoryRepository contractHistoryRepository;
	
	@Autowired(required=true)
	public JdbcContractService(ContractRepository contractRepository,
			RequestRepository requestRepository,
			ContractHistoryRepository contractHistoryRepository) {
		this.contractRepository = contractRepository;
		this.requestRepository = requestRepository;
		this.contractHistoryRepository = contractHistoryRepository;
	}

	@Override
	public PageVO readContractHistory(String senderDomain, String recipient,
			Integer limit, Integer offset) {
		return contractHistoryRepository.readContractHistory(senderDomain, recipient, limit, offset).createVO();
	}

	@Override
	public void removeContract(Long id) {
		contractRepository.removeContract(id);
	}

	@Override
	public PageVO readContracts(String senderDomain, String recipient,
			Integer limit, Integer offset) {
		return contractRepository.readContracts(senderDomain, recipient, limit, offset).createVO();
	}

	@Override
	public PageVO readRequests(String senderDomain, String recipient,
			Integer limit, Integer offset) {
		return requestRepository.readRequests(senderDomain, recipient, limit, offset).createVO();
	}

}
