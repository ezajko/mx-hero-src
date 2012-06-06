package org.mxhero.webapi.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mxhero.webapi.vo.LdapPropertyVO;
import org.springframework.jdbc.core.RowMapper;

public class LdapPropertyMapper implements RowMapper<LdapPropertyVO>{

	public static final String DATABASE = "mxhero";
	public static final String TABLE_NAME = "domain_adldap_properties";
	
	public static final String DOMAIN = "domain";
	public static final String PROPERTY_NAME = "property_name";
	public static final String PROPERTY_KEY = "property_key";
	
	@Override
	public LdapPropertyVO mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		LdapPropertyVO property = new LdapPropertyVO();
		property.setKey(rs.getString(PROPERTY_KEY));
		property.setName(rs.getString(PROPERTY_NAME));
		return property;
	}

}
