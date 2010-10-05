package org.mxhero.console.backend.service.jpa;

import java.util.Collection;

import org.mxhero.console.backend.dao.ApplicationUserDao;
import org.mxhero.console.backend.entity.ApplicationUser;
import org.mxhero.console.backend.service.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("applicationUserService")
@RemotingDestination(channels={"flex-amf"})
public class JpaApplicationUserService implements ApplicationUserService {

	private PasswordEncoder encoder;
	
	private ApplicationUserDao userDao;
	
	@Autowired
	public JpaApplicationUserService(ApplicationUserDao userDao, PasswordEncoder encoder){
		this.userDao = userDao;
		this.encoder = encoder;
	}

	@Secured("ROLE_DOMAIN_ADMIN")
	@Override
	public void changePassword(String userName, String password,
			String newPassword) {
		ApplicationUser user = userDao.finbByUserName(userName);
		user.setPassword(encoder.encodePassword(newPassword,null));
		userDao.save(user);
	}

	@Secured("ROLE_ADMIN")
	@Override
	public Collection<ApplicationUser> findAll() {
		return this.userDao.readAll();
	}

	@Secured("ROLE_DOMAIN_ADMIN")
	@Override
	public ApplicationUser findByUserName(String userName) {
		return userDao.finbByUserName(userName);
	}

	@Secured("ROLE_DOMAIN_ADMIN")
	@Override
	public void update(ApplicationUser applicationUser) {
		userDao.save(applicationUser);
	}

}
