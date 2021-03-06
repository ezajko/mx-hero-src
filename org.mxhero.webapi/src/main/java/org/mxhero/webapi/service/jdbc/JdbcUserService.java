package org.mxhero.webapi.service.jdbc;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.mxhero.webapi.infrastructure.RandomPassword;
import org.mxhero.webapi.infrastructure.mail.MailSender;
import org.mxhero.webapi.infrastructure.pagination.common.PageResult;
import org.mxhero.webapi.repository.AccountRepository;
import org.mxhero.webapi.repository.DomainRepository;
import org.mxhero.webapi.repository.SystemPropertyRepository;
import org.mxhero.webapi.repository.UserRepository;
import org.mxhero.webapi.service.UserService;
import org.mxhero.webapi.service.exception.UnknownResourceException;
import org.mxhero.webapi.vo.PageVO;
import org.mxhero.webapi.vo.SystemPropertyVO;
import org.mxhero.webapi.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("jdbcUserService")
public class JdbcUserService implements UserService{

	private static final String MAIL_PASSWORD_RECOVERY_SUBJECT="mail.password.recovery.subject";
	private static final String MAIL_PASSWORD_RECOVERY_BODY="mail.password.recovery.boby";
	private static final String REPLACE_USER="$USER$";
	private static final String REPLACE_PASSWORD="$PASSWORD$";
	
	private UserRepository userRepository;
	private SystemPropertyRepository systemPropertyRepository;
	private AbstractMessageSource ms;
	private PasswordEncoder encoder;
	private DomainRepository domainRepository;
	private AccountRepository accountRepository;
	
	@Autowired(required=true)
	public JdbcUserService(UserRepository userRepository, SystemPropertyRepository systemPropertyRepository, AbstractMessageSource ms, PasswordEncoder encoder, DomainRepository domainRepository, AccountRepository accountRepository) {
		this.userRepository = userRepository;
		this.systemPropertyRepository = systemPropertyRepository;
		this.ms = ms;
		this.encoder = encoder;
		this.domainRepository = domainRepository;
		this.accountRepository = accountRepository;
	}

	@Transactional(readOnly=true)
	@Override
	public PageVO<UserVO> readAll(String domain, Integer limit, Integer offset) {
		
		PageResult<UserVO> result = userRepository.findall(domain, offset, limit);
		PageVO<UserVO> userVOPage = new PageVO<UserVO>();
		userVOPage.setActualPage(result.getPageNumber());
		userVOPage.setTotalElements(result.getTotalRecordsNumber());
		userVOPage.setTotalPages(result.getPageAmount());
		userVOPage.setElements(result.getPageData());
		
		return userVOPage;
	}

	@Transactional(readOnly=false)
	@Override
	public UserVO create(UserVO user, String role) {
		if(role==null){
			throw new UnknownResourceException("user.create.role.empty");
		}
		if(role.equalsIgnoreCase(UserVO.ROLE_ADMIN)){
			user.setDomain(null);
			user.setAccount(null);
		}else if(role.equalsIgnoreCase(UserVO.ROLE_DOMAIN_ADMIN)){
			user.setAccount(null);
			if(user.getDomain()==null || user.getDomain().isEmpty()){
				throw new IllegalArgumentException("user.create.domain.empty");
			}
			if(domainRepository.findById(user.getDomain())==null){
				throw new UnknownResourceException("domain.not.found");
			}
		}else if(role.equalsIgnoreCase(UserVO.ROLE_DOMAIN_ACCOUNT)){
			if(user.getDomain()==null || user.getDomain().isEmpty()){
				throw new IllegalArgumentException("user.create.domain.empty");
			}
			if(domainRepository.findById(user.getDomain())==null){
				throw new UnknownResourceException("domain.not.found");
			}
			if(user.getAccount()==null || user.getAccount().isEmpty()){
				throw new IllegalArgumentException("user.create.account.empty");
			}
			if(accountRepository.findById(user.getAccount(), user.getDomain())==null){
				throw new IllegalArgumentException("domain.account.not.found");
			}
		}
		
		UserVO userCreated = userRepository.insert(user, role, encoder.encodePassword(user.getPassword(),null));
		return userCreated;
	}

	@Transactional(readOnly=false)
	@Override
	public void resetPassword(String email) {
		UserVO user = userRepository.finbByNotifyEmail(email);
		if(user==null){
			throw new UnknownResourceException("user.not.found");
		}
		Map<String,String> systemProperties = new HashMap<String, String>();
		String newPassword = RandomPassword.getRandomString(6);
		for(SystemPropertyVO property : systemPropertyRepository.findAll()){
			systemProperties.put(property.getKey(), property.getValue());
		}
		MailSender.send(
				ms.getMessage(MAIL_PASSWORD_RECOVERY_SUBJECT, null, new Locale(user.getLocale().split("_")[0],user.getLocale().split("_")[1])), 
				ms.getMessage(MAIL_PASSWORD_RECOVERY_BODY, null, new Locale(user.getLocale().split("_")[0],user.getLocale().split("_")[1])).replace(REPLACE_USER,user.getUserName()).replace(REPLACE_PASSWORD, newPassword), 
				user.getNotifyEmail(), 
				systemProperties);
		if(userRepository.setPassword( encoder.encodePassword(newPassword,null), user.getUserName())!=true){
			throw new UnknownResourceException("user.not.found");
		}
	}

	@Transactional(readOnly=true)
	@Override
	public UserVO read(String username) {
		UserVO user = userRepository.finbByUserName(username);
		if(user==null){
			throw new UnknownResourceException("user.not.found");
		}
		return user;
	}

	@Transactional(readOnly=false)
	@Override
	public void update(String username, UserVO user) {
		if(user==null){
			throw new IllegalArgumentException("user.is.null");
		}
		if(!username.equalsIgnoreCase(user.getUserName())){
			throw new IllegalArgumentException("user.not.match");
		}
		UserVO userInserted = userRepository.finbByUserName(username);
		if(userInserted==null 
				||(userInserted.getDomain()==null && user.getDomain()!=null)
				||(user.getDomain()==null && userInserted.getDomain()!=null)
				||(userInserted.getDomain()!=null && user.getDomain()!=null &&!userInserted.getDomain().equalsIgnoreCase(user.getDomain()))){
			throw new UnknownResourceException("user.not.found");
		}
		userRepository.update(user);
	}

	@Transactional(readOnly=false)
	@Override
	public void changePassword(String username, String oldPassword,
			String newPassword, String domain) {
		UserVO user = userRepository.finbByUserName(username);
		if(user==null){
			throw new UnknownResourceException("user.not.found");
		}
		if(user.getDomain()!=null){
			if(!user.getDomain().equalsIgnoreCase(domain)){
				throw new UnknownResourceException("user.not.found");
			}
		}else if(domain!=null){
			throw new UnknownResourceException("user.not.found");
		}
		if(userRepository.changePassword(oldPassword, newPassword, username)){
			throw new IllegalArgumentException("user.oldpassword.not.match");
		}
		userRepository.changePassword(encoder.encodePassword(oldPassword,null), encoder.encodePassword(newPassword,null), username);
	}

	@Transactional(readOnly=false)
	@Override
	public void delete(String username, String domain) {
		UserVO user = userRepository.finbByUserName(username);
		if(user==null){
			throw new UnknownResourceException("user.not.found");
		}
		if(user.getDomain()!=null){
			if(!user.getDomain().equalsIgnoreCase(domain)){
				throw new UnknownResourceException("user.not.found");
			}
		}else if(domain!=null){
			throw new UnknownResourceException("user.not.found");
		}
		userRepository.delete(username);
	}

	@Transactional(readOnly=false)
	@Override
	public void setPassword(String username, String newPassword) {
		UserVO user = userRepository.finbByUserName(username);
		if(user==null){
			throw new UnknownResourceException("user.not.found");
		}
		userRepository.setPassword(encoder.encodePassword(newPassword,null), username);
	}

	@Transactional(readOnly=true)
	@Override
	public UserVO readByAccount(String domain, String account) {
		UserVO user = userRepository.finbByAccount(domain, account);
		if(user==null){
			throw new UnknownResourceException("user.not.found");
		}
		return user;
	}

}
