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

import org.mxhero.console.backend.vo.RequestVO;
import org.springframework.jdbc.core.RowMapper;

public class RequestMapper implements RowMapper<RequestVO>{

	public static final String DATABASE="contracts";
	public static final String TABLE_NAME="rqt_request";
	
	public static final String ID="rqt_id";
	public static final String RECIPIENT="rqt_recipient";
	public static final String SENDER_DOMAIN="rqt_sender_domain";
	public static final String RULE_ID="rqt_rule_id";
	public static final String APPROVED_DATE="rqt_approved_date";
	public static final String DISCLAIMER_PLAIN="rqt_disclaimer_plain";
	public static final String DISCLAIMER_HTML="rqt_disclaimer_html";
	public static final String ADITIONAL_DATA="rqt_aditional_data";
	public static final String PATH="rqt_path";
	public static final String REQUEST_DATE="rqt_request_date";
	public static final String VETO_DATE="rqt_vote_date";
	public static final String PENDING="rqt_pending";
	public static final String MESSAGE_ID="rqt_message_id";
	public static final String TYPE="rqt_type";
	public static final String PHASE="rqt_queue_phase";
	public static final String RULE_PRIORITY="rqt_rule_priority";
	
	@Override
	public RequestVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		RequestVO requestVO = new RequestVO();
		
		requestVO.setAdditionalData(rs.getString(ADITIONAL_DATA));
		if(rs.getDate(APPROVED_DATE)!=null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(rs.getDate(APPROVED_DATE));
			requestVO.setApprovedDate(calendar);
		}		
		requestVO.setDisclaimerHtml(rs.getString(DISCLAIMER_HTML));
		requestVO.setDisclaimerPlain(rs.getString(DISCLAIMER_PLAIN));
		requestVO.setId(rs.getLong(ID));
		requestVO.setPath(rs.getString(PATH));
		requestVO.setRecipient(rs.getString(RECIPIENT));
		if(rs.getDate(REQUEST_DATE)!=null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(rs.getDate(REQUEST_DATE));
			requestVO.setRequestDate(calendar);
		}
		requestVO.setRuleId(rs.getLong(RULE_ID));
		requestVO.setSenderDomain(rs.getString(SENDER_DOMAIN));
		if(rs.getDate(VETO_DATE)!=null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(rs.getDate(VETO_DATE));
			requestVO.setVetoDate(calendar);
		}		
		requestVO.setPending(rs.getBoolean(PENDING));
		requestVO.setMessageId(rs.getString(MESSAGE_ID));
		requestVO.setType(rs.getString(TYPE));
		requestVO.setPhase(rs.getString(PHASE));
		requestVO.setRulePriority(rs.getInt(RULE_PRIORITY));
		
		return requestVO;
	}

}
