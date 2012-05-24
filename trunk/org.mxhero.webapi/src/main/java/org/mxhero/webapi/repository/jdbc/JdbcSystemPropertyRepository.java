package org.mxhero.webapi.repository.jdbc;

import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.mxhero.webapi.repository.SystemPropertyRepository;
import org.mxhero.webapi.repository.jdbc.mapper.SystemPropertyMapper;
import org.mxhero.webapi.vo.SystemPropertyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value="mxhero",readOnly=false)
public class JdbcSystemPropertyRepository implements SystemPropertyRepository{

	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcSystemPropertyRepository(@Qualifier("mxheroDataSource")DataSource ds) {
		this.template = new NamedParameterJdbcTemplate(ds);
	}
	
	@Override
	public List<SystemPropertyVO> findAll() {
		String sql = " SELECT `"+SystemPropertyMapper.KEY+"`,"+"`"+SystemPropertyMapper.VALUE+"`" +
				" FROM `"+SystemPropertyMapper.DATABASE+"`.`"+SystemPropertyMapper.TABLE_NAME+"` ;";
		return template.getJdbcOperations().query(sql, new SystemPropertyMapper());
	}

	@Override
	public SystemPropertyVO findById(String propertyKey) {
		String sql = " SELECT `"+SystemPropertyMapper.KEY+"`,"+"`"+SystemPropertyMapper.VALUE+"`" +
				" FROM `"+SystemPropertyMapper.DATABASE+"`.`"+SystemPropertyMapper.TABLE_NAME+"`" +
				" WHERE `"+SystemPropertyMapper.KEY+"` = :key ;";
		List<SystemPropertyVO> properties = template.query(sql, new MapSqlParameterSource("key",propertyKey),new SystemPropertyMapper());
		if(properties!=null && properties.size()>0){
			return properties.get(0);
		}
		return null;
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void save(SystemPropertyVO property) {
		String sql = "INSERT INTO `"+SystemPropertyMapper.DATABASE+"`.`"+SystemPropertyMapper.TABLE_NAME+"` " +
				" (`"+SystemPropertyMapper.KEY+"`,`"+SystemPropertyMapper.VALUE+"`) " +
				" VALUES (:key,:value)" +
				" ON DUPLICATE KEY UPDATE `"+SystemPropertyMapper.VALUE+"` = VALUES(`"+SystemPropertyMapper.VALUE+"`);";
		if(property!=null){
			MapSqlParameterSource source = new MapSqlParameterSource();
			source.addValue("key", property.getPropertyKey());
			source.addValue("value", property.getPropertyValue());
			template.update(sql, source);
		}
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void save(Collection<SystemPropertyVO> properties) {
		String sql = "INSERT INTO `"+SystemPropertyMapper.DATABASE+"`.`"+SystemPropertyMapper.TABLE_NAME+"` " +
				" (`"+SystemPropertyMapper.KEY+"`,`"+SystemPropertyMapper.VALUE+"`) " +
				" VALUES (:key,:value)" +
				" ON DUPLICATE KEY UPDATE `"+SystemPropertyMapper.VALUE+"` = VALUES(`"+SystemPropertyMapper.VALUE+"`);";
		if(properties!=null && properties.size()>0){
			template.batchUpdate(sql, propertiesToSource(properties));
		}
	}
	
	private MapSqlParameterSource[] propertiesToSource(Collection<SystemPropertyVO> properties){
		MapSqlParameterSource[] source = new MapSqlParameterSource[properties.size()];
		int i = 0;
		for(SystemPropertyVO property : properties){
			MapSqlParameterSource map = new MapSqlParameterSource();
			map.addValue("key", property.getPropertyKey());
			map.addValue("value", property.getPropertyValue());
			source[i]=map;
			++i;
		}
		return source;
	}

}
