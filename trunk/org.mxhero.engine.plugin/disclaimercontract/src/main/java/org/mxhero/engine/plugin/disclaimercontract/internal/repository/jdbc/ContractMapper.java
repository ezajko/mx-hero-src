package org.mxhero.engine.plugin.disclaimercontract.internal.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.mxhero.engine.plugin.disclaimercontract.entity.Contract;
import org.springframework.jdbc.core.RowMapper;

public class ContractMapper implements RowMapper<Contract>{

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
	public Contract mapRow(ResultSet rs, int rowNum) throws SQLException {
		Contract contract = new Contract();
		contract.setAditionalData(rs.getString(ADITIONAL_DATA));
		if(rs.getDate(APPROVED_DATE)!=null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(rs.getDate(APPROVED_DATE));
			contract.setApprovedDate(calendar);
		}
		contract.setDisclaimerHtml(rs.getString(DISCLAIMER_HTML));
		contract.setDisclaimerPlain(rs.getString(DISCLAIMER_PLAIN));
		contract.setId(rs.getLong(ID));
		contract.setRecipient(rs.getString(RECIPIENT));
		contract.setRuleId(rs.getLong(RULE_ID));
		contract.setSenderDomain(rs.getString(SENDER_DOMAIN));
	
		return contract;
	}

}
