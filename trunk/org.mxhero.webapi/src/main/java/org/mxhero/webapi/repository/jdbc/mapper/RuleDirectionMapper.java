package org.mxhero.webapi.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mxhero.webapi.vo.RuleDirectionVO;
import org.springframework.jdbc.core.RowMapper;

public class RuleDirectionMapper implements RowMapper<RuleDirectionVO>{

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
	public RuleDirectionVO mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		RuleDirectionVO direction = new RuleDirectionVO();
		direction.setAccount(rs.getString(ACCOUNT));
		direction.setDirectionType(rs.getString(DIRECTION_TYPE));
		direction.setDomain(rs.getString(DOMAIN));
		direction.setFreeValue(rs.getString(FREE_VALUE));
		direction.setGroup(rs.getString(GROUP_NAME));
		direction.setId(rs.getInt(ID));
		return direction;
	}

}
