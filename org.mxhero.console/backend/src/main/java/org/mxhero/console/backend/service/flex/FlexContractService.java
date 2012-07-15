package org.mxhero.console.backend.service.flex;

import org.mxhero.console.backend.service.ContractService;
import org.mxhero.console.backend.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service("contractService")
@RemotingDestination(channels = { "flex-amf" })
public class FlexContractService implements ContractService{

	private ContractService service;

	@Autowired(required=true)
	public FlexContractService(@Qualifier("jdbcContractService")ContractService service) {
		this.service = service;
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public PageVO readContractHistory(String senderDomain, String recipient,
			Integer limit, Integer offset) {
		return service.readContractHistory(senderDomain, recipient, limit, offset);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public void removeContract(Long id) {
		service.removeContract(id);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public PageVO readContracts(String senderDomain, String recipient,
			Integer limit, Integer offset) {
		return service.readContracts(senderDomain, recipient, limit, offset);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public PageVO readRequests(String senderDomain, String recipient,
			Integer limit, Integer offset) {
		return service.readRequests(senderDomain, recipient, limit, offset);
	}

}
