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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("jdbcUserDetailsService")
public class JdbcUserDetailsService implements UserDetailsService {

	private NamedParameterJdbcTemplate template;
	
	@Autowired(required=true)
	public JdbcUserDetailsService(@Qualifier("mxheroDataSource")DataSource ds){
		this.template = new NamedParameterJdbcTemplate(ds);
	}


	@Override
	@Transactional(value="mxhero",readOnly=true)
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		String sql = "SELECT `"+UserMapper.ID+"`,`"+UserMapper.PASSWORD+"`,`"+UserMapper.USER_NAME+"`,`"+UserMapper.ENABLED+"`" +
				" FROM `"+UserMapper.DATABASE+"`.`"+UserMapper.TABLE_NAME+"`" +
				" WHERE `"+UserMapper.USER_NAME+"` = :userName ;";
		CustomUser user = null;
		List<Map<String, Object>>  users = template.queryForList(sql, new MapSqlParameterSource("userName",username));
		if (users==null || users.size()<1){
			throw new UsernameNotFoundException("username:"+username);
		}
		
		Map<String, Object> userResult = users.get(0);
		List<AuthorityVO> authorities = getAuthorities((Integer)userResult.get(UserMapper.ID));
		user = new CustomUser((String)userResult.get(UserMapper.USER_NAME),(String)userResult.get(UserMapper.PASSWORD),(Boolean)userResult.get(UserMapper.ENABLED),authorities);
		return user;
	}

	private List<AuthorityVO> getAuthorities(Integer userId){
		String sql = "SELECT `"+AuthorityMapper.TABLE_NAME+"`.`"+AuthorityMapper.ID+"`," +
				" `"+AuthorityMapper.TABLE_NAME+"`.`"+AuthorityMapper.AUTHORITY+"`" +
				" FROM `"+AuthorityMapper.DATABASE+"`.`"+AuthorityMapper.TABLE_NAME+"`" +
				" INNER JOIN `"+AuthorityMapper.DATABASE+"`.`"+AuthorityMapper.MTM_USERS_TABLE+"`" +
				" ON `"+AuthorityMapper.MTM_USERS_TABLE+"`.`"+AuthorityMapper.MTM_USERS_AUTHORITY_ID+"` = `"+AuthorityMapper.TABLE_NAME+"`.`"+AuthorityMapper.ID+"` " +
				" WHERE  `"+AuthorityMapper.MTM_USERS_TABLE+"`.`"+AuthorityMapper.MTM_USERS_USER_ID+"` = :userId;";
		return template.query(sql, new MapSqlParameterSource("userId",userId), new AuthorityMapper());
	}
	
}