package org.mxhero.engine.plugin.threadlight.internal.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRowFollower;
import org.springframework.jdbc.core.RowMapper;

public class ThreadRowFollowerMapper implements RowMapper<ThreadRowFollower>{

	public static final String DATABASE="mxhero";
	public static final String TABLE_NAME="thread_light_threads_follower";
	
	public static final String THREAD_ID="thread_id";
	public static final String FOLLOWER="follower";
	public static final String MESSAGE_ID="message_id";

	@Override
	public ThreadRowFollower mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		ThreadRowFollower follower = new ThreadRowFollower();
		
		follower.setMessageId(rs.getString(MESSAGE_ID));
		follower.setFollower(rs.getString(FOLLOWER));
		follower.setThreadRowId(rs.getLong(THREAD_ID));
		
		return follower;
	}
}
