package org.mxhero.feature.disclaimercontract.provider.internal.repository;

import java.util.List;

import javax.sql.DataSource;

import org.mxhero.feature.disclaimercontract.provider.internal.entity.Approval;
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
	public Approval save(Approval aproval) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Approval> findByRule(Long rule, String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Approval findByRuleAndRecipient(Long rule, String recipient,
			String type) {
		// TODO Auto-generated method stub
		return null;
	}

}
