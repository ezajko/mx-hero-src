package org.mxhero.console.backend.security;

import java.util.Calendar;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.console.backend.dao.ApplicationUserDao;
import org.mxhero.console.backend.dao.AuthorityDao;
import org.mxhero.console.backend.entity.ApplicationUser;
import org.mxhero.console.backend.entity.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/app-config-test.xml"})
public class JpaUserDetailsServiceTest {

	
	private static final String USER_NAME = "admin";
	
	private static final String PASSWORD = "password";

	@Autowired
	private ApplicationUserDao applicationUserDao;
	
	@Autowired
	private AuthorityDao authorityDao;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private AuthenticationManager manager;
	
	@Autowired
	private JpaUserDetailsService service;
	
	@Before
	@Transactional
	public void before(){
		if(applicationUserDao.finbByUserName(USER_NAME)==null){
			ApplicationUser user = new ApplicationUser();
			user.setUserName(USER_NAME);
			user.setPassword(encoder.encodePassword(PASSWORD, null));
			user.setLastName("MyLastName");
			user.setName("MyName");
			user.setCreationDate(Calendar.getInstance());
			user.setEnabled(true);
			user.setLocale("pt_BR");
			user.setNotifyEmail("email@example.com");
			Calendar valid = Calendar.getInstance();
			valid.add(Calendar.DAY_OF_MONTH, 30);
			user.setValidUntil(valid);

			user.setAuthorities(new HashSet<Authority>(authorityDao.readAll()));
			user = applicationUserDao.save(user);

		}
	}
	
	@Test
	public void test(){
		UserDetails userDetails = service.loadUserByUsername(USER_NAME);
		Assert.assertNotNull(userDetails);
		Authentication request = new UsernamePasswordAuthenticationToken(USER_NAME, PASSWORD);
        Authentication result = manager.authenticate(request);
        SecurityContextHolder.getContext().setAuthentication(result);
        Assert.assertTrue(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
	}
	
}
