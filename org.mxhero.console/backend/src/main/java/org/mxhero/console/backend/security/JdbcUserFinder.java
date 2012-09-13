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

package org.mxhero.console.backend.security;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.mxhero.console.backend.repository.jdbc.mapper.AuthorityMapper;
import org.mxhero.console.backend.repository.jdbc.mapper.UserMapper;
import org.mxhero.console.backend.vo.AuthorityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("jdbcUserFinder")
public class JdbcUserFinder implements UserFinder{

	private NamedParameterJdbcTemplate template;
	
	@Autowired(required=true)
	public JdbcUserFinder(@Qualifier("mxheroDataSource")DataSource ds){
		this.template = new NamedParameterJdbcTemplate(ds);
	}

	@Transactional(value="mxhero",readOnly=true)
	public Map<String, Object> loadUserByUsername(String username)
			throws DataAccessException {
		String sql = "SELECT `"+UserMapper.ID+"`,`"+UserMapper.PASSWORD+"`,`"+UserMapper.USER_NAME+"`,`"+UserMapper.ENABLED+"`" +
				" FROM `"+UserMapper.DATABASE+"`.`"+UserMapper.TABLE_NAME+"`" +
				" WHERE `"+UserMapper.USER_NAME+"` = :userName ;";
		List<Map<String, Object>>  users = template.queryForList(sql, new MapSqlParameterSource("userName",username));
		if (users==null || users.size()<1){
			return null;
		}
		
		Map<String, Object> userResult = users.get(0);
		return userResult;
	}

	public List<AuthorityVO> getAuthorities(Integer userId){
		String sql = "SELECT `"+AuthorityMapper.TABLE_NAME+"`.`"+AuthorityMapper.ID+"`," +
				" `"+AuthorityMapper.TABLE_NAME+"`.`"+AuthorityMapper.AUTHORITY+"`" +
				" FROM `"+AuthorityMapper.DATABASE+"`.`"+AuthorityMapper.TABLE_NAME+"`" +
				" INNER JOIN `"+AuthorityMapper.DATABASE+"`.`"+AuthorityMapper.MTM_USERS_TABLE+"`" +
				" ON `"+AuthorityMapper.MTM_USERS_TABLE+"`.`"+AuthorityMapper.MTM_USERS_AUTHORITY_ID+"` = `"+AuthorityMapper.TABLE_NAME+"`.`"+AuthorityMapper.ID+"` " +
				" WHERE  `"+AuthorityMapper.MTM_USERS_TABLE+"`.`"+AuthorityMapper.MTM_USERS_USER_ID+"` = :userId;";
		return template.query(sql, new MapSqlParameterSource("userId",userId), new AuthorityMapper());
	}
}
