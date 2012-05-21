package org.mxhero.webapi.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class AuthorityMapper implements RowMapper<String>{

	public static final String MTM_USERS_TABLE = "app_users_authorities";
	public static final String MTM_USERS_USER_ID = "app_users_id";
	public static final String MTM_USERS_AUTHORITY_ID = "authorities_id";
	
	public static final String DATABASE="mxhero";
	public static final String TABLE_NAME="authorities";
	public static final String ID="id";
	public static final String AUTHORITY="authority";
	
	@Override
	public String mapRow(ResultSet rs, int param) throws SQLException {
		return rs.getString(AUTHORITY);
	}

}
