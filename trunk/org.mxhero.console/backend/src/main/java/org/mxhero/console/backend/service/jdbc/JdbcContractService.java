/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
