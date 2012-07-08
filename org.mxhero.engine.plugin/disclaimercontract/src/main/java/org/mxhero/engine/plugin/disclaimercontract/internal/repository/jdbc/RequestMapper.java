package org.mxhero.engine.plugin.disclaimercontract.internal.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.mxhero.engine.plugin.disclaimercontract.entity.Request;
import org.springframework.jdbc.core.RowMapper;

public class RequestMapper implements RowMapper<Request>{

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
	public Request mapRow(ResultSet rs, int rowNum) throws SQLException {
		Request request = new Request();
		
		request.setAdditionalData(rs.getString(ADITIONAL_DATA));
		if(rs.getDate(APPROVED_DATE)!=null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(rs.getDate(APPROVED_DATE));
			request.setApprovedDate(calendar);
		}		
		request.setDisclaimerHtml(rs.getString(DISCLAIMER_HTML));
		request.setDisclaimerPlain(rs.getString(DISCLAIMER_PLAIN));
		request.setId(rs.getLong(ID));
		request.setPath(rs.getString(PATH));
		request.setRecipient(rs.getString(RECIPIENT));
		if(rs.getDate(REQUEST_DATE)!=null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(rs.getDate(REQUEST_DATE));
			request.setRequestDate(calendar);
		}
		request.setRuleId(rs.getLong(RULE_ID));
		request.setSenderDomain(rs.getString(SENDER_DOMAIN));
		if(rs.getDate(VETO_DATE)!=null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(rs.getDate(VETO_DATE));
			request.setVetoDate(calendar);
		}		
		request.setPending(rs.getBoolean(PENDING));
		request.setMessageId(rs.getString(MESSAGE_ID));
		request.setType(rs.getString(TYPE));
		request.setPhase(rs.getString(PHASE));
		request.setRulePriority(rs.getInt(RULE_PRIORITY));
		
		return request;
	}

}
