package org.mxhero.console.backend.service.jpa;

import java.util.ArrayList;
import java.util.Collection;

import org.mxhero.console.backend.dao.SystemPropertyDao;
import org.mxhero.console.backend.entity.SystemProperty;
import org.mxhero.console.backend.service.ConfigurationService;
import org.mxhero.console.backend.vo.ConfigurationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;

@Service("configurationService")
@RemotingDestination(channels={"flex-amf"})
public class JpaConfigurationService implements ConfigurationService{

	private SystemPropertyDao systemPropertyDao;
	
	@Autowired
	public JpaConfigurationService(SystemPropertyDao systemPropertyDao) {
		super();
		this.systemPropertyDao = systemPropertyDao;
	}

	@Override
	public void edit(ConfigurationVO configurationVO) {
		SystemProperty property = null;
		Collection<SystemProperty> properties = new ArrayList<SystemProperty>();
		
		property = systemPropertyDao.findByKey(SystemProperty.MAIL_SMTP_AUTH);
		property.setValue(configurationVO.getAuth().toString());
		properties.add(property);
		
		property = systemPropertyDao.findByKey(SystemProperty.DEFAULT_USER_LANGUAGE);
		property.setValue(configurationVO.getDefaultLanguage());
		properties.add(property);
		
		property = systemPropertyDao.findByKey(SystemProperty.MAIL_SMTP_HOST);
		property.setValue(configurationVO.getHost());
		properties.add(property);
		
		property = systemPropertyDao.findByKey(SystemProperty.MAIL_SMTP_PASSWORD);
		property.setValue(configurationVO.getPassword());
		properties.add(property);
		
		property = systemPropertyDao.findByKey(SystemProperty.MAIL_SMTP_PORT);
		property.setValue(configurationVO.getPort().toString());
		properties.add(property);
		
		property = systemPropertyDao.findByKey(SystemProperty.MAIL_SMTP_SSL_ENABLE);
		property.setValue(configurationVO.getSsl().toString());
		properties.add(property);
		
		property = systemPropertyDao.findByKey(SystemProperty.MAIL_SMTP_USER);
		property.setValue(configurationVO.getUser());
		properties.add(property);

		property = systemPropertyDao.findByKey(SystemProperty.MAIL_ADMIN);
		property.setValue(configurationVO.getAdminMail());
		properties.add(property);
		
		systemPropertyDao.save(properties);
	}

	@Override
	public ConfigurationVO find() {
		ConfigurationVO configuration = new ConfigurationVO();
		
		configuration.setAuth(Boolean.parseBoolean(systemPropertyDao.findByKey(SystemProperty.MAIL_SMTP_AUTH).getValue()));
		configuration.setDefaultLanguage(systemPropertyDao.findByKey(SystemProperty.DEFAULT_USER_LANGUAGE).getValue());
		configuration.setHost(systemPropertyDao.findByKey(SystemProperty.MAIL_SMTP_HOST).getValue());
		configuration.setPassword(systemPropertyDao.findByKey(SystemProperty.MAIL_SMTP_PASSWORD).getValue());
		configuration.setPort(Integer.parseInt(systemPropertyDao.findByKey(SystemProperty.MAIL_SMTP_PORT).getValue()));		
		configuration.setSsl(Boolean.parseBoolean(systemPropertyDao.findByKey(SystemProperty.MAIL_SMTP_SSL_ENABLE).getValue()));
		configuration.setUser(systemPropertyDao.findByKey(SystemProperty.MAIL_SMTP_USER).getValue());
		configuration.setAdminMail(systemPropertyDao.findByKey(SystemProperty.MAIL_ADMIN).getValue());
		
		return configuration;
	}

}
