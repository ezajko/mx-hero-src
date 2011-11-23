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

		return feature;
	}

}
