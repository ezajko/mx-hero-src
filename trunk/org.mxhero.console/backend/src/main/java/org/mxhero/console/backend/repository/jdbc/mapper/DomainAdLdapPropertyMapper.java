package org.mxhero.console.backend.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mxhero.console.backend.vo.DomainAdLdapPropertyVO;
import org.springframework.jdbc.core.RowMapper;

public class DomainAdLdapPropertyMapper implements RowMapper<DomainAdLdapPropertyVO>{

	public static final String DATABASE = "mxhero";
	public static final String TABLE_NAME = "domain_adldap_properties";
	
	public static final String DOMAIN = "domain";
	public static final String PROPERTY_NAME = "property_name";
	public static final String PROPERTY_KEY = "property_key";
	
	@Override
	public DomainAdLdapPropertyVO mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		DomainAdLdapPropertyVO property = new DomainAdLdapPropertyVO();
		property.setKey(rs.getString(PROPERTY_KEY));
		property.setName(rs.getString(PROPERTY_NAME));
		return property;
	}

}
