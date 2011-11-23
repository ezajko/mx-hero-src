package org.mxhero.console.backend.repository.jdbc;

import java.util.List;

import javax.sql.DataSource;

import org.mxhero.console.backend.repository.FeatureRepository;
import org.mxhero.console.backend.repository.jdbc.mapper.CategoryMapper;
import org.mxhero.console.backend.repository.jdbc.mapper.FeatureMapper;
import org.mxhero.console.backend.vo.CategoryVO;
import org.mxhero.console.backend.vo.FeatureVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value="mxhero",readOnly=false)
public class JdbcFeatureRepository implements FeatureRepository{

	private static final String SQL_FEATURE = 
			"SELECT `"+FeatureMapper.COMPONENT+"`,`"+FeatureMapper.DEFAULT_ADMIN_ORDER+"`," +
			" `"+FeatureMapper.DESCRIPTION_KEY+"`, `"+FeatureMapper.EXPLAIN_KEY+"`," +
			" `"+FeatureMapper.ID+"`, `"+FeatureMapper.LABEL_KEY+"`," +
			" `"+FeatureMapper.MODULE_REPORT_URL+"`, `"+FeatureMapper.MODULE_URL+"`" +
			" FROM `"+FeatureMapper.DATABASE+"`.`"+FeatureMapper.TABLE_NAME+"`";

	private static final String SQL_CATEGORY = 
			"SELECT `"+CategoryMapper.ICON_SOURCE+"`,`"+CategoryMapper.LABEL_KEY+"`,`"+CategoryMapper.ID+"`" +
			" FROM `"+CategoryMapper.DATABASE+"`.`"+CategoryMapper.TABLE_NAME+"`";
	
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcFeatureRepository(@Qualifier("mxheroDataSource")DataSource ds) {
		this.template =  new NamedParameterJdbcTemplate(ds);
	}
	
	@Override
	public FeatureVO findById(Integer featureId) {
		String sql = SQL_FEATURE + " WHERE `"+FeatureMapper.ID+"`";
		if(featureId == null){
			sql = sql + " IS NULL ;";
		}else {
			sql = sql + " = :featureId ;";
		}
		MapSqlParameterSource source = new MapSqlParameterSource("featureId",featureId);
		List<FeatureVO> features = template.query(sql, source, new FeatureMapper());
		if(features!=null && features.size()>0){
			return features.get(0);
		}
		return null;
	}

	@Override
	public List<FeatureVO> findFeatures(Integer categoryId) {
		String sql = SQL_FEATURE + " WHERE `"+FeatureMapper.CATEGORY_ID+"`";
		if(categoryId == null){
			sql = sql + " IS NULL ;";
		}else {
			sql = sql + " = :categoryId ;";
		}
		MapSqlParameterSource source = new MapSqlParameterSource("categoryId",categoryId);
		return template.query(sql, source, new FeatureMapper());
	}

	@Override
	public List<CategoryVO> findAll() {
		String sql = SQL_CATEGORY + " ORDER BY `"+CategoryMapper.ID+"` ASC";
		return template.getJdbcOperations().query(sql, new CategoryMapper());
	}

}
