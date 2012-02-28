package org.mxhero.engine.plugin.threadlight.internal.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mxhero.engine.plugin.threadlight.vo.ThreadRowFollower;
import org.springframework.jdbc.core.RowMapper;

public class ThreadRowFollowerMapper implements RowMapper<ThreadRowFollower>{

	public static final String DATABASE="threadlight";
	public static final String TABLE_NAME="thread_rows_followers";
	
	public static final String THREAD_ID="thread_id";
	public static final String FOLLOWER="follower";
	public static final String PARAMETERS="follower_parameters";

	@Override
	public ThreadRowFollower mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		ThreadRowFollower follower = new ThreadRowFollower();
		follower.setFollower(rs.getString(FOLLOWER));
		follower.setFolowerParameters(rs.getString(PARAMETERS));
		return follower;
	}
}
