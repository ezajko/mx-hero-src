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

import org.mxhero.console.backend.vo.FeatureVO;
import org.springframework.jdbc.core.RowMapper;

public class FeatureMapper implements RowMapper<FeatureVO>{

	public static final String DATABASE="mxhero";
	public static final String TABLE_NAME="features";
	
	public static final String ID="id";
	public static final String BASE_PRIORITY="base_priority";
	public static final String COMPONENT="component";
	public static final String DEFAULT_ADMIN_ORDER="default_admin_order";
	public static final String DESCRIPTION_KEY="description_key";
	public static final String EXPLAIN_KEY="explain_key";
	public static final String LABEL_KEY="label_key";
	public static final String MODULE_REPORT_URL="module_report_url";
	public static final String MODULE_URL="module_url";
	public static final String VERSION="version";
	public static final String CATEGORY_ID="category_id";
	public static final String ENABLED="enabled";
	
	@Override
	public FeatureVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		FeatureVO feature = new FeatureVO();
		
		feature.setComponent(rs.getString(COMPONENT));
		feature.setDefaultAdminOrder(rs.getString(DEFAULT_ADMIN_ORDER));
		feature.setDescription(rs.getString(DESCRIPTION_KEY));
		feature.setExplain(rs.getString(EXPLAIN_KEY));
		feature.setId(rs.getInt(ID));
		feature.setLabel(rs.getString(LABEL_KEY));
		feature.setModuleReportUrl(rs.getString(MODULE_REPORT_URL));
		feature.setModuleUrl(rs.getString(MODULE_URL));
		feature.setEnabled(rs.getBoolean(ENABLED));

		return feature;
	}

}
