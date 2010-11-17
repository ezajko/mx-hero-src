package org.mxhero.console.backend.service.jpa;

import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;

import org.mxhero.console.backend.dao.ApplicationUserDao;
import org.mxhero.console.backend.entity.ApplicationUser;
import org.mxhero.console.backend.infrastructure.BusinessException;
import org.mxhero.console.backend.infrastructure.MailSender;
import org.mxhero.console.backend.infrastructure.RandomPassword;
import org.mxhero.console.backend.service.ApplicationUserService;
import org.mxhero.console.backend.service.ConfigurationService;
import org.mxhero.console.backend.translator.ApplicationUserTranslator;
import org.mxhero.console.backend.vo.ApplicationUserVO;
import org.mxhero.console.backend.vo.ConfigurationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("applicationUserService")
@RemotingDestination(channels={"flex-amf"})
public class JpaApplicationUserService implements ApplicationUserService {

	private final static String MAIL_NOT_FOUND="mail.not.found";
	
	private final static String PASSWORD_NOT_MATCH="password.not.match";
	
	private PasswordEncoder encoder;
	
	private ApplicationUserDao userDao;
	
	private ApplicationUserTranslator applicationUserTranslator;
	
	private AbstractMessageSource ms;
	
	private ConfigurationService configurationService;
	
	private static final String MAIL_PASSWORD_RECOVERY_SUBJECT="mail.password.recovery.subject";
	private static final String MAIL_PASSWORD_RECOVERY_BODY="mail.password.recovery.boby";
	private static final String REPLACE_USER="$USER$";
	private static final String REPLACE_PASSWORD="$PASSWORD$";
	
	@Autowired
	public JpaApplicationUserService(PasswordEncoder encoder,
			ApplicationUserDao userDao,
			ApplicationUserTranslator applicationUserTranslator,
			AbstractMessageSource ms,
			ConfigurationService configurationService) {
		super();
		this.encoder = encoder;
		this.userDao = userDao;
		this.applicationUserTranslator = applicationUserTranslator;
		this.ms = ms;
		this.configurationService = configurationService;
	}


	@Override
	public void changePassword(String oldPassword, String newPassword) {
		ApplicationUser user = userDao.finbByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
		if(user.getPassword().equals(encoder.encodePassword(oldPassword,null))){
			user.setPassword(encoder.encodePassword(newPassword,null));
			userDao.save(user);
		} else {
			throw new BusinessException(PASSWORD_NOT_MATCH);
		}
		
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
	public ApplicationUserVO edit(ApplicationUserVO applicationUserVO) {
		ApplicationUser user = userDao.readByPrimaryKey(applicationUserVO.getId());
		user.setName(applicationUserVO.getName());
		user.setLastName(applicationUserVO.getLastName());
		user.setNotifyEmail(applicationUserVO.getNotifyEmail());
		user.setLocale(applicationUserVO.getLocale());
		return applicationUserTranslator.translate(userDao.save(user));
	}

	@Override
	public void sendPassword(String email) {

		ApplicationUser user = userDao.finbByNotifyEmail(email);
		if(user==null){
			throw new BusinessException(MAIL_NOT_FOUND);
		}
		
		ConfigurationVO config = configurationService.find();
		
		user.setPassword(RandomPassword.getRandomString(6));
		
		MailSender.send(
				ms.getMessage(MAIL_PASSWORD_RECOVERY_SUBJECT, null, new Locale(user.getLocale().split("_")[0],user.getLocale().split("_")[1])), 
				ms.getMessage(MAIL_PASSWORD_RECOVERY_BODY, null, new Locale(user.getLocale().split("_")[0],user.getLocale().split("_")[1])).replace(REPLACE_USER,user.getUserName()).replace(REPLACE_PASSWORD, user.getPassword()), 
				user.getNotifyEmail(), 
				config);
		
		user.setPassword(encoder.encodePassword(user.getPassword(),null));
		user.setLastPasswordUpdate(Calendar.getInstance());
		
		userDao.save(user);
	}
	

}
