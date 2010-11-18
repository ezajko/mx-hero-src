package org.mxhero.console.backend.service.jpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.mxhero.console.backend.dao.ApplicationUserDao;
import org.mxhero.console.backend.dao.SystemPropertyDao;
import org.mxhero.console.backend.entity.ApplicationUser;
import org.mxhero.console.backend.entity.SystemProperty;
import org.mxhero.console.backend.infrastructure.MailSender;
import org.mxhero.console.backend.service.ConfigurationService;
import org.mxhero.console.backend.vo.ConfigurationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("configurationService")
@RemotingDestination(channels={"flex-amf"})
public class JpaConfigurationService implements ConfigurationService{

	private static final String MAIL_TEST_SUBJECT="mail.test.subject";
	private static final String MAIL_TEST_BODY="mail.test.body";
	
	private SystemPropertyDao systemPropertyDao;
	
	private AbstractMessageSource ms;
	
	private ApplicationUserDao userDao;
	
	@Autowired
	public JpaConfigurationService(SystemPropertyDao systemPropertyDao, AbstractMessageSource ms,ApplicationUserDao userDao) {
		super();
		this.systemPropertyDao = systemPropertyDao;
		this.ms=ms;
		this.userDao=userDao;
	}

	@Override
	public void edit(ConfigurationVO configurationVO) {
		SystemProperty property = null;
		Collection<SystemProperty> properties = new ArrayList<SystemProperty>();
		
		property = systemPropertyDao.findByKey(SystemProperty.MAIL_SMTP_AUTH);
		property.setPropertyValue(configurationVO.getAuth().toString());
		properties.add(property);
		
		property = systemPropertyDao.findByKey(SystemProperty.MAIL_SMTP_HOST);
		property.setPropertyValue(configurationVO.getHost());
		properties.add(property);
		
		property = systemPropertyDao.findByKey(SystemProperty.MAIL_SMTP_PASSWORD);
		if(configurationVO.getAuth()){
			property.setPropertyValue(configurationVO.getPassword());
		} else {
			property.setPropertyValue(null);
		}
		properties.add(property);
		
		property = systemPropertyDao.findByKey(SystemProperty.MAIL_SMTP_PORT);
		property.setPropertyValue(configurationVO.getPort().toString());
		properties.add(property);
		
		property = systemPropertyDao.findByKey(SystemProperty.MAIL_SMTP_SSL_ENABLE);
		property.setPropertyValue(configurationVO.getSsl().toString());
		properties.add(property);
		
		property = systemPropertyDao.findByKey(SystemProperty.MAIL_SMTP_USER);
		if(configurationVO.getAuth()){
			property.setPropertyValue(configurationVO.getUser());
		} else {
			property.setPropertyValue(null);
		}
		properties.add(property);

		property = systemPropertyDao.findByKey(SystemProperty.MAIL_ADMIN);
		property.setPropertyValue(configurationVO.getAdminMail());
		properties.add(property);
		
		systemPropertyDao.save(properties);
	}

	@Override
	public ConfigurationVO find() {
		ConfigurationVO configuration = new ConfigurationVO();
		
		configuration.setAuth(Boolean.parseBoolean(systemPropertyDao.findByKey(SystemProperty.MAIL_SMTP_AUTH).getPropertyValue()));
		configuration.setDefaultLanguage(systemPropertyDao.findByKey(SystemProperty.DEFAULT_USER_LANGUAGE).getPropertyValue());
		configuration.setHost(systemPropertyDao.findByKey(SystemProperty.MAIL_SMTP_HOST).getPropertyValue());
		configuration.setPassword(systemPropertyDao.findByKey(SystemProperty.MAIL_SMTP_PASSWORD).getPropertyValue());
		configuration.setPort(Integer.parseInt(systemPropertyDao.findByKey(SystemProperty.MAIL_SMTP_PORT).getPropertyValue()));		
		configuration.setSsl(Boolean.parseBoolean(systemPropertyDao.findByKey(SystemProperty.MAIL_SMTP_SSL_ENABLE).getPropertyValue()));
		configuration.setUser(systemPropertyDao.findByKey(SystemProperty.MAIL_SMTP_USER).getPropertyValue());
		configuration.setAdminMail(systemPropertyDao.findByKey(SystemProperty.MAIL_ADMIN).getPropertyValue());
		
		return configuration;
	}

	@Override
	public void testMail(ConfigurationVO configurationVO) {
		ApplicationUser user = userDao.finbByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
		MailSender.send(
				ms.getMessage(MAIL_TEST_SUBJECT, null, new Locale(user.getLocale().split("_")[0],user.getLocale().split("_")[1])), 
				ms.getMessage(MAIL_TEST_BODY, null, new Locale(user.getLocale().split("_")[0],user.getLocale().split("_")[1])), 
				configurationVO.getAdminMail(), 
				configurationVO);
		
	}

}
