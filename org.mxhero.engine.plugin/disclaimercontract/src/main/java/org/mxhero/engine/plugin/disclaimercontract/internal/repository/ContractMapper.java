package org.mxhero.engine.plugin.disclaimercontract.internal.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mxhero.engine.plugin.disclaimercontract.entity.Contract;
import org.springframework.jdbc.core.RowMapper;

public class ContractMapper implements RowMapper<Contract>{

	@Override
	public Contract mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
