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

import java.util.List;

import javax.sql.DataSource;

import org.mxhero.console.backend.repository.QuarantineRepository;
import org.mxhero.console.backend.repository.jdbc.mapper.QuarantineMapper;
import org.mxhero.console.backend.vo.QuarantineVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value="mxhero",readOnly=true)
public class JdbcQuarantineRepository implements QuarantineRepository{

	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcQuarantineRepository(@Qualifier("mxheroDataSource")DataSource ds) {
		this.template =  new NamedParameterJdbcTemplate(ds);
	}
	
	@Override
	@Transactional(value="mxhero",readOnly=false)
	public int save(QuarantineVO qurantine) {
		String sql = "INSERT INTO `"+QuarantineMapper.DATABASE+"`.`"+QuarantineMapper.TABLE_NAME+"`"+
						" (`"+QuarantineMapper.DOMAIN+"`,`"+QuarantineMapper.EMAIL+"`)"+
						" VALUES(:domainId,:email)"+
						" ON DUPLICATE KEY UPDATE `"+QuarantineMapper.EMAIL+"`=VALUES("+QuarantineMapper.EMAIL+")";
		MapSqlParameterSource source = new MapSqlParameterSource("domainId",qurantine.getDomain());
		source.addValue("email", qurantine.getEmail());
		return template.update(sql, source);
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public int delete(String domain) {
		String sql = "DELETE FROM `"+QuarantineMapper.DATABASE+"`.`"+QuarantineMapper.TABLE_NAME+"`" +
				" WHERE `"+QuarantineMapper.DOMAIN+"` = :domainId";
		return template.update(sql, new MapSqlParameterSource("domainId",domain));
	}

	@Override
	@Transactional(value="mxhero",readOnly=true)
	public QuarantineVO read(String domain) {
		String sql = "SELECT `"+QuarantineMapper.DOMAIN+"`,`"+QuarantineMapper.EMAIL+"`"+
				" FROM `"+QuarantineMapper.DATABASE+"`.`"+QuarantineMapper.TABLE_NAME+"`" +
				" WHERE `"+QuarantineMapper.DOMAIN+"` = :domainId";
		List<QuarantineVO> result = template.query(sql,  new MapSqlParameterSource("domainId",domain), new QuarantineMapper());
		if(result!=null && result.size()>0){
			return result.get(0);
		}
		return null;
	}

}
