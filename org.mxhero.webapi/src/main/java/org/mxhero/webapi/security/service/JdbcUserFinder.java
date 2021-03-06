package org.mxhero.webapi.security.service;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.mxhero.webapi.repository.jdbc.mapper.AuthorityMapper;
import org.mxhero.webapi.repository.jdbc.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("jdbcUserFinder")
public class JdbcUserFinder implements UserFinder{
	private static Logger log = LoggerFactory.getLogger(JdbcUserFinder.class);
	private NamedParameterJdbcTemplate template;

	@Autowired(required=true)
	public JdbcUserFinder(@Qualifier("mxheroDataSource")DataSource ds){
		this.template = new NamedParameterJdbcTemplate(ds);
	}

	@Transactional(value="mxhero",readOnly=true)
	public Map<String, Object> loadUserByUsername(String username)
			throws DataAccessException {
		log.debug("USERNAME:"+username);
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

	public List<String> getAuthorities(Integer userId){
		String sql = "SELECT `"+AuthorityMapper.TABLE_NAME+"`.`"+AuthorityMapper.ID+"`," +
				" `"+AuthorityMapper.TABLE_NAME+"`.`"+AuthorityMapper.AUTHORITY+"`" +
				" FROM `"+AuthorityMapper.DATABASE+"`.`"+AuthorityMapper.TABLE_NAME+"`" +
				" INNER JOIN `"+AuthorityMapper.DATABASE+"`.`"+AuthorityMapper.MTM_USERS_TABLE+"`" +
				" ON `"+AuthorityMapper.MTM_USERS_TABLE+"`.`"+AuthorityMapper.MTM_USERS_AUTHORITY_ID+"` = `"+AuthorityMapper.TABLE_NAME+"`.`"+AuthorityMapper.ID+"` " +
				" WHERE  `"+AuthorityMapper.MTM_USERS_TABLE+"`.`"+AuthorityMapper.MTM_USERS_USER_ID+"` = :userId;";
		return template.query(sql, new MapSqlParameterSource("userId",userId), new AuthorityMapper());
	}
}
