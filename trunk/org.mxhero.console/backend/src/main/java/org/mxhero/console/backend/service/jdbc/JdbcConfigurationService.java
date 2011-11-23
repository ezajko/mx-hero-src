package org.mxhero.console.backend.service.jdbc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.mxhero.console.backend.infrastructure.MailSender;
import org.mxhero.console.backend.repository.SystemPropertyRepository;
import org.mxhero.console.backend.repository.UserRepository;
import org.mxhero.console.backend.service.ConfigurationService;
import org.mxhero.console.backend.vo.ApplicationUserVO;
import org.mxhero.console.backend.vo.ConfigurationVO;
import org.mxhero.console.backend.vo.SystemPropertyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("configurationService")
@RemotingDestination(channels={"flex-amf"})
public class JdbcConfigurationService implements ConfigurationService{

	private static final String MAIL_TEST_SUBJECT="mail.test.subject";
	private static final String MAIL_TEST_BODY="mail.test.body";
	
	private SystemPropertyRepository systemPropertiesRepository;
	
	private AbstractMessageSource ms;
	
	private UserRepository userRepository;
	
	@Autowired
	public JdbcConfigurationService(SystemPropertyRepository systemPropertiesRepository, 
			AbstractMessageSource ms,
			UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
		this.ms=ms;
		this.systemPropertiesRepository=systemPropertiesRepository;
	}

	@Override
	public void edit(ConfigurationVO configurationVO) {
		SystemPropertyVO property = null;
		Collection<SystemPropertyVO> properties = new ArrayList<SystemPropertyVO>();
		
		property = new SystemPropertyVO();
		property.setPropertyKey(SystemPropertyVO.MAIL_SMTP_AUTH);
		property.setPropertyValue(configurationVO.getAuth().toString());
		properties.add(property);
		
		property = new SystemPropertyVO();
		property.setPropertyKey(SystemPropertyVO.MAIL_SMTP_HOST);
		property.setPropertyValue(configurationVO.getHost());
		properties.add(property);
		
		property = new SystemPropertyVO();
		property.setPropertyKey(SystemPropertyVO.MAIL_SMTP_PASSWORD);
		if(configurationVO.getAuth()){
			property.setPropertyValue(configurationVO.getPassword());
		} else {
			property.setPropertyValue(null);
		}
		properties.add(property);
		
		property = new SystemPropertyVO();
		property.setPropertyKey(SystemPropertyVO.MAIL_SMTP_PORT);
		property.setPropertyValue(configurationVO.getPort().toString());
		properties.add(property);
		
		property = new SystemPropertyVO();
		property.setPropertyKey(SystemPropertyVO.MAIL_SMTP_SSL_ENABLE);
		property.setPropertyValue(configurationVO.getSsl().toString());
		properties.add(property);
		
		property = new SystemPropertyVO();
		property.setPropertyKey(SystemPropertyVO.MAIL_SMTP_USER);
		if(configurationVO.getAuth()){
			property.setPropertyValue(configurationVO.getUser());
		} else {
			property.setPropertyValue(null);
		}
		properties.add(property);

		property = new SystemPropertyVO();
		property.setPropertyKey(SystemPropertyVO.MAIL_ADMIN);
		property.setPropertyValue(configurationVO.getAdminMail());
		properties.add(property);
		
		systemPropertiesRepository.save(properties);
	}

	@Override
	public ConfigurationVO find() {
		ConfigurationVO configuration = new ConfigurationVO();
		
		configuration.setAuth(Boolean.parseBoolean(systemPropertiesRepository.findById(SystemPropertyVO.MAIL_SMTP_AUTH).getPropertyValue()));
		configuration.setDefaultLanguage(systemPropertiesRepository.findById(SystemPropertyVO.DEFAULT_USER_LANGUAGE).getPropertyValue());
		configuration.setHost(systemPropertiesRepository.findById(SystemPropertyVO.MAIL_SMTP_HOST).getPropertyValue());
		configuration.setPassword(systemPropertiesRepository.findById(SystemPropertyVO.MAIL_SMTP_PASSWORD).getPropertyValue());
		configuration.setPort(Integer.parseInt(systemPropertiesRepository.findById(SystemPropertyVO.MAIL_SMTP_PORT).getPropertyValue()));		
		configuration.setSsl(Boolean.parseBoolean(systemPropertiesRepository.findById(SystemPropertyVO.MAIL_SMTP_SSL_ENABLE).getPropertyValue()));
		configuration.setUser(systemPropertiesRepository.findById(SystemPropertyVO.MAIL_SMTP_USER).getPropertyValue());
		configuration.setAdminMail(systemPropertiesRepository.findById(SystemPropertyVO.MAIL_ADMIN).getPropertyValue());
		SystemPropertyVO property = systemPropertiesRepository.findById(SystemPropertyVO.EXTERNAL_LOGO_PATH);
		if(property!=null){
			configuration.setLogoPath(property.getPropertyValue());
		}
		property=systemPropertiesRepository.findById(SystemPropertyVO.NEWS_FEED_ENABLED);
		if(property!=null){
			configuration.setNewsFeedEnabled(property.getPropertyValue().toLowerCase());
		}
		return configuration;
	}

	@Override
	public void testMail(ConfigurationVO configurationVO) {
		ApplicationUserVO user = userRepository.finbByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
		MailSender.send(
				ms.getMessage(MAIL_TEST_SUBJECT, null, new Locale(user.getLocale().split("_")[0],user.getLocale().split("_")[1])), 
				ms.getMessage(MAIL_TEST_BODY, null, new Locale(user.getLocale().split("_")[0],user.getLocale().split("_")[1])), 
				configurationVO.getAdminMail(), 
				configurationVO);
		
	}

}
