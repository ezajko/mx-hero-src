package org.mxhero.console.backend.security;

import org.mxhero.console.backend.dao.ApplicationUserDao;
import org.mxhero.console.backend.entity.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JpaUserDetailsService implements UserDetailsService {

	private ApplicationUserDao userDao;
	
	@Autowired
	public JpaUserDetailsService(ApplicationUserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		CustomUser user = null;
		ApplicationUser applicationUser = userDao.finbByUserName(username);
		if (applicationUser==null){
			throw new UsernameNotFoundException("username:"+username);
		}
		user = new CustomUser(applicationUser);
		return user;
	}

	
	
}