/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mxhero.console.backend.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.mxhero.console.backend.vo.FeatureRuleDirectionVO;
import org.mxhero.console.backend.vo.FeatureRuleVO;
import org.springframework.jdbc.core.RowMapper;

public class FeatureRuleMapper implements RowMapper<FeatureRuleVO>{

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
	public FeatureRuleVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		FeatureRuleVO rule = new FeatureRuleVO();
		rule.setAdminOrder(rs.getString(ADMIN_ORDER));
		if(rs.getTimestamp(CREATED)!=null){
			Calendar created = Calendar.getInstance();
			created.setTimeInMillis(rs.getTimestamp(CREATED).getTime());
			rule.setCreated(created);
		}
		
		rule.setFromDirection(new FeatureRuleDirectionVO());
		rule.getFromDirection().setId(rs.getInt(FROM_DIRECTION_ID));
		
		rule.setToDirection(new FeatureRuleDirectionVO());
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
