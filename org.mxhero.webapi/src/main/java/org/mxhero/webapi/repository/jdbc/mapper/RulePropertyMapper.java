package org.mxhero.webapi.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mxhero.webapi.vo.RulePropertyVO;
import org.springframework.jdbc.core.RowMapper;

public class RulePropertyMapper implements RowMapper<RulePropertyVO>{

	public static final String DATABASE="mxhero";
	public static final String TABLE_NAME="features_rules_properties";
	
	public static final String ID="id";
	public static final String PROPERTY_KEY="property_key";
	public static final String PROPERTY_VALUE="property_value";
	public static final String RULE_ID="rule_id";
	
	@Override
	public RulePropertyVO mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		RulePropertyVO property = new RulePropertyVO();
		property.setId(rs.getInt(ID));
		property.setPropertyKey(rs.getString(PROPERTY_KEY));
		property.setPropertyValue(rs.getString(PROPERTY_VALUE));
		return property;
	}
	
}
