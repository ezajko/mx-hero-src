package org.mxhero.webapi.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.mxhero.webapi.vo.RuleDirectionVO;
import org.mxhero.webapi.vo.RuleVO;
import org.springframework.jdbc.core.RowMapper;

public class RuleMapper implements RowMapper<RuleVO>{

	public static final String DATABASE="mxhero";
	public static final String TABLE_NAME="features_rules";
	
	public static final String ID="id";
	public static final String ADMIN_ORDER="admin_order";
	public static final String CREATED="created";
	public static final String ENABLED="enabled";
	public static final String LABEL="label";
	public static final String TWO_WAYS="two_ways";
	public static final String UPDATED="updated";
	public static final String DOMAIN_ID="domain_id";
	public static final String FEATURE_ID="feature_id";
	public static final String FROM_DIRECTION_ID="from_direction_id";
	public static final String TO_DIRECTION_ID="to_direction_id";
	
	@Override
	public RuleVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		RuleVO rule = new RuleVO();
		rule.setAdminOrder(rs.getString(ADMIN_ORDER));
		if(rs.getTimestamp(CREATED)!=null){
			Calendar created = Calendar.getInstance();
			created.setTimeInMillis(rs.getTimestamp(CREATED).getTime());
			rule.setCreated(created);
		}
		
		rule.setFromDirection(new RuleDirectionVO());
		rule.getFromDirection().setId(rs.getInt(FROM_DIRECTION_ID));
		
		rule.setToDirection(new RuleDirectionVO());
		rule.getToDirection().setId(rs.getInt(TO_DIRECTION_ID));
		
		rule.setFeatureId(rs.getInt(FEATURE_ID));
		rule.setEnabled(rs.getBoolean(ENABLED));
		rule.setId(rs.getInt(ID));
		rule.setName(rs.getString(LABEL));
		rule.setTwoWays(rs.getBoolean(TWO_WAYS));
		rule.setDomain(rs.getString(DOMAIN_ID));
		if(rs.getTimestamp(UPDATED)!=null){
			Calendar updated = Calendar.getInstance();
			updated.setTimeInMillis(rs.getTimestamp(UPDATED).getTime());
			rule.setUpdated(updated);
		}
		return rule;
	}
	
}
