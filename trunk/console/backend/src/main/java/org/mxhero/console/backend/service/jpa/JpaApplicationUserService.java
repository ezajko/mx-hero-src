package org.mxhero.console.backend.service.jpa;

import java.util.Collection;

import org.mxhero.console.backend.dao.ApplicationUserDao;
import org.mxhero.console.backend.entity.ApplicationUser;
import org.mxhero.console.backend.service.ApplicationUserService;
import org.mxhero.console.backend.translator.ApplicationUserTranslator;
import org.mxhero.console.backend.vo.ApplicationUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("applicationUserService")
@RemotingDestination(channels={"flex-amf"})
public class JpaApplicationUserService implements ApplicationUserService {

	private PasswordEncoder encoder;
	
	private ApplicationUserDao userDao;
	
	private ApplicationUserTranslator applicationUserTranslator;
	
	@Autowired
	public JpaApplicationUserService(ApplicationUserDao userDao, PasswordEncoder encoder, ApplicationUserTranslator applicationUserTranslator){
		this.userDao = userDao;
		this.encoder = encoder;
		this.applicationUserTranslator=applicationUserTranslator;
	}

	@Override
	public void changePassword(String newPassword) {
		ApplicationUser user = userDao.finbByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
		user.setPassword(encoder.encodePassword(newPassword,null));
		userDao.save(user);
	}

	@Override
	public void changeUserPassword(String userName, String password, String newPassword) {
		ApplicationUser user = userDao.finbByUserName(userName);
		user.setPassword(encoder.encodePassword(newPassword,null));
		userDao.save(user);
	}
	
	@Override
	public Collection<ApplicationUserVO> findAll() {
		return applicationUserTranslator.translate(this.userDao.readAll());
	}

	@Override
	public ApplicationUserVO findByUserName(String userName) {
		return applicationUserTranslator.translate(userDao.finbByUserName(userName));
	}
	
	@Override
	public ApplicationUserVO getUser() {
		
		return applicationUserTranslator.translate(userDao.finbByUserName(SecurityContextHolder.getContext().getAuthentication().getName()));
	}

	@Override
	public void update(ApplicationUserVO applicationUserVO) {
		//userDao.save(applicationUser);
	}

}
