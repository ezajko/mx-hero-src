package org.mxhero.engine.plugin.disclaimercontract.internal.repository;

import java.util.List;

import javax.sql.DataSource;

import org.mxhero.engine.plugin.disclaimercontract.entity.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class JdbcContractRepository implements ContractRepository{

	private static Logger log = LoggerFactory.getLogger(JdbcContractRepository.class);
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcContractRepository(DataSource ds) {
		this.template = new NamedParameterJdbcTemplate(ds);
	}

	@Override
	public List<Contract> findByRule(Long rule, String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Contract findByRuleAndRecipient(Long rule, String recipient,
			String type) {
		// TODO Auto-generated method stub
		return null;
	}

}
