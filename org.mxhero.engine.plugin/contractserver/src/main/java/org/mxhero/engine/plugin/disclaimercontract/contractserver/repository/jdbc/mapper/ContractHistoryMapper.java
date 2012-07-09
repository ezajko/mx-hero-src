package org.mxhero.engine.plugin.disclaimercontract.contractserver.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mxhero.engine.plugin.disclaimercontract.contractserver.vo.ContractHistoryVO;
import org.mxhero.engine.plugin.disclaimercontract.contractserver.vo.ContractVO;
import org.springframework.jdbc.core.RowMapper;

public class ContractHistoryMapper implements RowMapper<ContractHistoryVO>{

	public static final String DATABASE="contracts";
	public static final String TABLE_NAME="cth_contracts_history";
	public static final String ACTION = "cth_action";
	public static final String ACTION_DATE = "cth_action_date";
	
	
	@Override
	public ContractHistoryVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		ContractHistoryVO historyVO = new ContractHistoryVO();
		
		ContractVO contractVO = new ContractMapper().mapRow(rs, rowNum);
		historyVO.setContract(contractVO);
		historyVO.setAction(rs.getString(ACTION));
		historyVO.setActionDate(rs.getString(ACTION_DATE));
		
		return historyVO;
	}

}
