package org.mxhero.engine.plugin.emailquarantine.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class QuarantineMapper implements RowMapper<QuarantineVO>{

	public static final String DATABASE = "mxhero";
	public static final String TABLE_NAME = "quarantine";
	
	public static final String DOMAIN = "domain_id";
	public static final String EMAIL = "email";
	
	@Override
	public QuarantineVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		QuarantineVO quarantine = new QuarantineVO();
		
		quarantine.setDomain(rs.getString(DOMAIN));
		quarantine.setEmail(rs.getString(EMAIL));
		
		return quarantine;
	}

}
