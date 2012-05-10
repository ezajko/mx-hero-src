package org.mxhero.console.backend.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.mxhero.console.backend.vo.RecordVO;
import org.springframework.jdbc.core.RowMapper;

public class RecordMapper implements RowMapper<RecordVO>{

	public static final String DATABASE="statistics";
	public static final String TABLE_NAME="mail_records";
	
	public static final String INSERT_DATE="insert_date";
	public static final String RECORD_SEQUENCE="record_sequence";
	public static final String SERVER_NAME="server_name";
	public static final String BCC_RECIPIENTS="bcc_recipients";
	public static final String BYTES_SIZE="bytes_size";
	public static final String CC_RECIPIENTS="cc_recipients";
	public static final String FROM_RECIPIENTS="from_recipients";
	public static final String MESSAGE_ID="message_id";
	public static final String NG_RECIPIENTS="ng_recipients";
	public static final String PHASE="phase";
	public static final String RECIPIENT="recipient";
	public static final String RECIPIENT_DOMAIN_ID="recipient_domain_id";
	public static final String RECIPIENT_ID="recipient_id";
	public static final String SENDER="sender";
	public static final String SENDER_DOMAIN_ID="sender_domain_id";
	public static final String SENDER_ID="sender_id";
	public static final String STATE="state";
	public static final String STATE_REASON="state_reason";
	public static final String SUBJECT="subject";
	public static final String TO_RECIPIENTS="to_recipients";
	public static final String FLOW="flow";
	public static final String SENDER_GROUP="sender_group";
	public static final String RECIPIENT_GROUP="recipient_group";
	
	@Override
	public RecordVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		RecordVO record = new RecordVO();
		
		record.setBccRecipients(rs.getString(BCC_RECIPIENTS));
		record.setBytesSize(rs.getInt(BYTES_SIZE));
		record.setCcRecipients(rs.getString(CC_RECIPIENTS));
		record.setFlow(rs.getString(FLOW));
		record.setFrom(rs.getString(FROM_RECIPIENTS));
		if(rs.getTimestamp(INSERT_DATE)!=null){
			Calendar insertDate = Calendar.getInstance();
			insertDate.setTimeInMillis(rs.getTimestamp(INSERT_DATE).getTime());
			record.setInsertDate(insertDate);
		}
		record.setMessageId(rs.getString(MESSAGE_ID));
		record.setNgRecipients(rs.getString(NG_RECIPIENTS));
		record.setPhase(rs.getString(PHASE));
		record.setRecipient(rs.getString(RECIPIENT));
		record.setSender(rs.getString(SENDER));
		record.setState(rs.getString(STATE));
		record.setStateReason(rs.getString(STATE_REASON));
		record.setSubject(rs.getString(SUBJECT));
		record.setToRecipients(rs.getString(TO_RECIPIENTS));
		record.setSequence(rs.getLong(RECORD_SEQUENCE));
		
		return record;
	}
}
