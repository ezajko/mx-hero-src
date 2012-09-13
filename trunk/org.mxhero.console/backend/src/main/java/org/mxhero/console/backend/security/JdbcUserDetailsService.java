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