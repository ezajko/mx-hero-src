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

import org.mxhero.console.backend.vo.ContractVO;
import org.springframework.jdbc.core.RowMapper;

public class ContractMapper implements RowMapper<ContractVO>{

	public static final String DATABASE="contracts";
	public static final String TABLE_NAME="ctt_contracts";
	
	public static final String ID="ctt_id";
	public static final String RECIPIENT="ctt_recipient";
	public static final String SENDER_DOMAIN="ctt_sender_domain";
	public static final String RULE_ID="ctt_rule_id";
	public static final String APPROVED_DATE="ctt_approved_date";
	public static final String DISCLAIMER_PLAIN="ctt_disclaimer_plain";
	public static final String DISCLAIMER_HTML="ctt_disclaimer_html";
	public static final String ADITIONAL_DATA="ctt_aditional_data";
	public static final String REQUEST_DATE="rqt_request_date";
	
	@Override
	public ContractVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		ContractVO contractVO = new ContractVO();
		contractVO.setAditionalData(rs.getString(ADITIONAL_DATA));
		if(rs.getDate(APPROVED_DATE)!=null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(rs.getDate(APPROVED_DATE));
			contractVO.setApprovedDate(calendar);
		}
		contractVO.setDisclaimerHtml(rs.getString(DISCLAIMER_HTML));
		contractVO.setDisclaimerPlain(rs.getString(DISCLAIMER_PLAIN));
		contractVO.setId(rs.getLong(ID));
		contractVO.setRecipient(rs.getString(RECIPIENT));
		contractVO.setRuleId(rs.getLong(RULE_ID));
		contractVO.setSenderDomain(rs.getString(SENDER_DOMAIN));
	
		return contractVO;
	}

}
