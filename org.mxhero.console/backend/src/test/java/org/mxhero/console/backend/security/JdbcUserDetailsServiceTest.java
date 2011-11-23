package org.mxhero.console.backend.security;

import java.util.Calendar;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.console.backend.repository.UserRepository;
import org.mxhero.console.backend.vo.ApplicationUserVO;
import org.mxhero.console.backend.vo.AuthorityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/app-config-test.xml"})
public class JdbcUserDetailsServiceTest {

	
	private static final String USER_NAME = "admin";
	
	private static final String PASSWORD = "password";

	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager manager;
	
	@Autowired
	private UserDetailsService service;
	
	@Before
	public void before(){
		String password = encoder.encodePassword(PASSWORD, null);
		if(userRepository.finbByUserName(USER_NAME)==null){
			ApplicationUserVO user = new ApplicationUserVO();
			user.setUserName(USER_NAME);
			user.setLastName("MyLastName");
			user.setName("MyName");
			user.setCreationDate(Calendar.getInstance());
			user.setLocale("pt_BR");
			user.setNotifyEmail("email@example.com");
			user.setAuthorities(new HashSet<AuthorityVO>(userRepository.findAllAuthorities()));
			user = userRepository.insert(user,password);
		}else{
			userRepository.setPassword(password, userRepository.finbByUserName(USER_NAME).getId());
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
