package org.mxhero.console.backend.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.mxhero.console.backend.vo.ContractHistoryVO;
import org.mxhero.console.backend.vo.ContractVO;
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
		if(rs.getDate(ACTION_DATE)!=null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(rs.getDate(ACTION_DATE));
			historyVO.setActionDate(calendar);
		}
		
		return historyVO;
	}

}
