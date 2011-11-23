package org.mxhero.console.backend.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mxhero.console.backend.vo.FeatureRuleDirectionVO;
import org.springframework.jdbc.core.RowMapper;

public class FeatureRuleDirectionMapper implements RowMapper<FeatureRuleDirectionVO>{

	public static final String DATABASE="mxhero";
	public static final String TABLE_NAME="features_rules_directions";
	
	public static final String ID="id";
	public static final String ACCOUNT="account";
	public static final String DIRECTION_TYPE="directiom_type";
	public static final String DOMAIN="domain";
	public static final String FREE_VALUE="free_value";
	public static final String GROUP_NAME="group_name";
	public static final String RULE_ID="rule_id";
	
	@Override
	public FeatureRuleDirectionVO mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		FeatureRuleDirectionVO direction = new FeatureRuleDirectionVO();
		direction.setAccount(rs.getString(ACCOUNT));
		direction.setDirectionType(rs.getString(DIRECTION_TYPE));
		direction.setDomain(rs.getString(DOMAIN));
		direction.setFreeValue(rs.getString(FREE_VALUE));
		direction.setGroup(rs.getString(GROUP_NAME));
		direction.setId(rs.getInt(ID));
		return direction;
	}

}
