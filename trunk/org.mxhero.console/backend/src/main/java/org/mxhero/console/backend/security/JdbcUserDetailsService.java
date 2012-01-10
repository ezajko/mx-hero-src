package org.mxhero.console.backend.security;

import java.util.List;
import java.util.Map;

import org.mxhero.console.backend.repository.jdbc.mapper.UserMapper;
import org.mxhero.console.backend.vo.AuthorityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("jdbcUserDetailsService")
public class JdbcUserDetailsService implements UserDetailsService {

	private UserFinder userFinder;
	
	@Autowired(required=true)
	public JdbcUserDetailsService(UserFinder userFinder){
		this.userFinder = userFinder;
	}

	@Override
	@Transactional(value="mxhero",readOnly=true)
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		CustomUser user = null;
		Map<String, Object> userResult = userFinder.loadUserByUsername(username);
		if(userResult==null){
			throw new UsernameNotFoundException("username:"+username);
		}
		List<AuthorityVO> authorities = userFinder.getAuthorities((Integer)userResult.get(UserMapper.ID));
		user = new CustomUser((String)userResult.get(UserMapper.USER_NAME),(String)userResult.get(UserMapper.PASSWORD),(Boolean)userResult.get(UserMapper.ENABLED),authorities);
		return user;
	}
	
}