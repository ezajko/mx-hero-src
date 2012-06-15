package org.mxhero.webapi.security.service;

import java.util.List;
import java.util.Map;

import org.mxhero.webapi.repository.jdbc.mapper.UserMapper;
import org.mxhero.webapi.security.CustomUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("jdbcUserDetailsService")
public class JdbcUserDetailsService implements UserDetailsService {

	private static Logger log = LoggerFactory.getLogger(JdbcUserDetailsService.class);
	
	private UserFinder userFinder;
	
	@Autowired(required=true)
	public JdbcUserDetailsService(UserFinder userFinder){
		this.userFinder = userFinder;
	}

	@Override
	@Transactional(value="mxhero",readOnly=true)
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		log.debug("USERNAME:"+username);
		CustomUser user = null;
		Map<String, Object> userResult = userFinder.loadUserByUsername(username);
		if(userResult==null){
			throw new UsernameNotFoundException("username:"+username);
		}
		List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userFinder.getAuthorities((Integer)userResult.get(UserMapper.ID)).toArray(new String[]{}));
		user = new CustomUser((String)userResult.get(UserMapper.USER_NAME),(String)userResult.get(UserMapper.PASSWORD),(Boolean)userResult.get(UserMapper.ENABLED),authorities);
		return user;
	}
	
}