package org.mxhero.console.backend.service.jdbc;

import java.util.Locale;

import org.mxhero.console.backend.infrastructure.BusinessException;
import org.mxhero.console.backend.infrastructure.MailSender;
import org.mxhero.console.backend.infrastructure.RandomPassword;
import org.mxhero.console.backend.repository.DomainAdLdapRepository;
import org.mxhero.console.backend.repository.DomainRepository;
import org.mxhero.console.backend.repository.UserRepository;
import org.mxhero.console.backend.service.ApplicationUserService;
import org.mxhero.console.backend.service.ConfigurationService;
import org.mxhero.console.backend.vo.ApplicationUserVO;
import org.mxhero.console.backend.vo.ConfigurationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

@Repository("jdbcApplicationUserService")
public class JdbcApplicationUserService implements ApplicationUserService {

	private final static String MAIL_NOT_FOUND="mail.not.found";
	
	private final static String PASSWORD_NOT_MATCH="password.not.match";
	
	private PasswordEncoder encoder;
	
	private AbstractMessageSource ms;
	
	private UserRepository userRepository;
	
	private DomainRepository domainRepository;
	
	private DomainAdLdapRepository adLdapRepository;
	
	private ConfigurationService configurationService;
	
	private static final String MAIL_PASSWORD_RECOVERY_SUBJECT="mail.password.recovery.subject";
	private static final String MAIL_PASSWORD_RECOVERY_BODY="mail.password.recovery.boby";
	private static final String REPLACE_USER="$USER$";
	private static final String REPLACE_PASSWORD="$PASSWORD$";
	
	@Autowired
	public JdbcApplicationUserService(PasswordEncoder encoder,
			UserRepository userRepository,
			DomainRepository domainRepository,
			AbstractMessageSource ms,
			@Qualifier("jdbcConfigurationService")ConfigurationService configurationService,
			DomainAdLdapRepository adLdapRepository) {
		super();
		this.encoder = encoder;
		this.ms = ms;
		this.userRepository = userRepository;
		this.domainRepository = domainRepository;
		this.configurationService = configurationService;
		this.adLdapRepository = adLdapRepository;
	}


	@Override
	public void changePassword(String oldPassword, String newPassword) {
		ApplicationUserVO user = userRepository.finbByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
		changePassword(oldPassword, newPassword,user.getId());
	}
	
	@Override
	public void changePassword(String oldPassword, String newPassword, Integer id) {
		boolean changed = userRepository.changePassword(encoder.encodePassword(oldPassword,null), encoder.encodePassword(newPassword,null), id);
		if(!changed){
			throw new BusinessException(PASSWORD_NOT_MATCH);
		}
	}

	
	@Override
	public ApplicationUserVO getUser() {
		ApplicationUserVO user = userRepository.finbByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
		if(user!=null){
			user.setDomain(domainRepository.findByUserId(user.getId()));
			if(user.getDomain()!=null){
				user.getDomain().setAdLdap(adLdapRepository.findByDomainId(user.getDomain().getDomain()));
				user.getDomain().setAliases(domainRepository.findAliases(user.getDomain().getDomain()));
			}
		}
		return user;
	}

	@Override
	public ApplicationUserVO edit(ApplicationUserVO applicationUserVO) {
		ApplicationUserVO user = null;
		userRepository.update(applicationUserVO);
		user = userRepository.finbById(applicationUserVO.getId());
		if(user!=null){
			user.setDomain(domainRepository.findByUserId(user.getId()));
		}
		return user;
	}

	@Override
	public void sendPassword(String email) {
		ApplicationUserVO user = userRepository.finbByNotifyEmail(email);
		if(user==null){
			throw new BusinessException(MAIL_NOT_FOUND);
		}
		ConfigurationVO config = configurationService.find();
		String newPassword = RandomPassword.getRandomString(6);
		MailSender.send(
				ms.getMessage(MAIL_PASSWORD_RECOVERY_SUBJECT, null, new Locale(user.getLocale().split("_")[0],user.getLocale().split("_")[1])), 
				ms.getMessage(MAIL_PASSWORD_RECOVERY_BODY, null, new Locale(user.getLocale().split("_")[0],user.getLocale().split("_")[1])).replace(REPLACE_USER,user.getUserName()).replace(REPLACE_PASSWORD, newPassword), 
				user.getNotifyEmail(), 
				config);
		userRepository.setPassword(newPassword, user.getId());
	}


	@Override
	public boolean isAuthenticated() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth==null){
			return false;
		}
		return auth.isAuthenticated();
	}
	

}
