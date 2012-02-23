package org.mxhero.engine.plugin.threadlight.internal.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRowPk;
import org.springframework.jdbc.core.RowMapper;

public class ThreadRowMapper implements RowMapper<ThreadRow>{


	public static final String DATABASE="threadlight";
	public static final String TABLE_NAME="thread_rows";
	
	public static final String ID="id";
	public static final String MESSAGE_ID="message_id";
	public static final String CREATION_TIME="creation_time";
	public static final String RECIPIENT_MAIL="recipient_mail";
	public static final String SENDER_MAIL="sender_mail";
	public static final String SUBJECT="message_subject";
	public static final String REPLY_TIME="reply_time";
	public static final String SNOOZE_TIME="snooze_time";
	
	@Override
	public ThreadRow mapRow(ResultSet rs, int rowNum) throws SQLException {
		ThreadRow row = new ThreadRow();
		
		if(rs.getTimestamp(CREATION_TIME)!=null){
			row.setCreationTime(rs.getTimestamp(CREATION_TIME));
		}
		if(rs.getTimestamp(REPLY_TIME)!=null){
			row.setReplyTime(rs.getTimestamp(REPLY_TIME));
		}
		if(rs.getTimestamp(SNOOZE_TIME)!=null){
			row.setSnoozeTime(rs.getTimestamp(SNOOZE_TIME));
		}
		row.setId(rs.getLong(ID));
		row.setPk(new ThreadRowPk(rs.getString(MESSAGE_ID), rs.getString(SENDER_MAIL), rs.getString(RECIPIENT_MAIL)));
		row.setSubject(rs.getString(SUBJECT));
		
		return row;
	}

}
