package org.mxhero.console.backend.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mxhero.console.backend.vo.FeatureRulePropertyVO;
import org.springframework.jdbc.core.RowMapper;

public class FeatureRulePropertyMapper implements RowMapper<FeatureRulePropertyVO>{

	public static final String DATABASE="mxhero";
	public static final String TABLE_NAME="features_rules_properties";
	
	public static final String ID="id";
	public static final String PROPERTY_KEY="property_key";
	public static final String PROPERTY_VALUE="property_value";
	public static final String RULE_ID="rule_id";
	
	@Override
	public FeatureRulePropertyVO mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		FeatureRulePropertyVO property = new FeatureRulePropertyVO();
		property.setId(rs.getInt(ID));
		property.setPropertyKey(rs.getString(PROPERTY_KEY));
		property.setPropertyValue(rs.getString(PROPERTY_VALUE));
		return property;
	}
	
}
