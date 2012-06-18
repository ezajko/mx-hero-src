package org.mxhero.webapi.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mxhero.webapi.vo.SystemPropertyVO;
import org.springframework.jdbc.core.RowMapper;

public class SystemPropertyMapper implements RowMapper<SystemPropertyVO>{

	public static final String DATABASE="mxhero";
	public static final String TABLE_NAME="system_properties";
	
	public static final String KEY="property_key";
	public static final String VALUE="property_value";
	
	@Override
	public SystemPropertyVO mapRow(ResultSet rs, int param)
			throws SQLException {
		SystemPropertyVO systemProperty = new SystemPropertyVO();
		
		systemProperty.setKey(rs.getString(KEY));
		systemProperty.setValue(rs.getString(VALUE));
		
		return systemProperty;
	}

}
