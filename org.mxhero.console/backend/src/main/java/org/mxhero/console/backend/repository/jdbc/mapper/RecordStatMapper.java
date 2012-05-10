package org.mxhero.console.backend.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mxhero.console.backend.vo.RecordStatVO;
import org.springframework.jdbc.core.RowMapper;

public class RecordStatMapper implements RowMapper<RecordStatVO>{

	public static final String DATABASE="statistics";
	public static final String TABLE_NAME="mail_stats";
	
	public static final String STAT_KEY="stat_key";
	public static final String STAT_VALUE="stat_value";
	public static final String RECORD_SEQUENCE="record_sequence";
	public static final String INSERT_DATE="insert_date";
	
	@Override
	public RecordStatVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		RecordStatVO stat = new RecordStatVO();
		stat.setKey(rs.getString(STAT_KEY));
		stat.setValue(rs.getString(STAT_VALUE));
		return stat;
	}
	
}
