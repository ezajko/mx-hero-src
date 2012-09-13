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
