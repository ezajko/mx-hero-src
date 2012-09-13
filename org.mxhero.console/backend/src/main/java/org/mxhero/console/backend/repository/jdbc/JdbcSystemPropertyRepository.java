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

package org.mxhero.console.backend.repository.jdbc;

import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.mxhero.console.backend.repository.SystemPropertyRepository;
import org.mxhero.console.backend.repository.jdbc.mapper.SystemPropertyMapper;
import org.mxhero.console.backend.vo.SystemPropertyVO;
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
