package org.mxhero.console.backend.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mxhero.console.backend.vo.CategoryVO;
import org.springframework.jdbc.core.RowMapper;

public class CategoryMapper implements RowMapper<CategoryVO>{

	public static final String DATABASE="mxhero";
	public static final String TABLE_NAME="features_categories";
	
	public static final String ID="id";
	public static final String ICON_SOURCE="icon_source";
	public static final String LABEL_KEY="label_key";
	
	@Override
	public CategoryVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		CategoryVO category = new CategoryVO();
		
		category.setIconsrc(rs.getString(ICON_SOURCE));
		category.setId(rs.getInt(ID));
		category.setLabel(rs.getString(LABEL_KEY));
		
		return category;
	}

}
