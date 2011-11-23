package org.mxhero.console.backend.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mxhero.console.backend.vo.AuthorityVO;
import org.springframework.jdbc.core.RowMapper;

public class AuthorityMapper implements RowMapper<AuthorityVO>{

	public static final String MTM_USERS_TABLE = "app_users_authorities";
	public static final String MTM_USERS_USER_ID = "app_users_id";
	public static final String MTM_USERS_AUTHORITY_ID = "authorities_id";
	
	public static final String DATABASE="mxhero";
	public static final String TABLE_NAME="authorities";
	public static final String ID="id";
	public static final String AUTHORITY="authority";
	
	@Override
	public AuthorityVO mapRow(ResultSet rs, int param) throws SQLException {
		AuthorityVO authority = new AuthorityVO();
		authority.setAuthority(rs.getString(AUTHORITY));
		authority.setId(rs.getInt(ID));
		return authority;
	}

}
