package org.mxhero.console.backend.service.flex;

import org.mxhero.console.backend.service.ConfigurationService;
import org.mxhero.console.backend.vo.ConfigurationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service("configurationService")
@RemotingDestination(channels={"flex-amf"})
public class FlexConfigurationService implements ConfigurationService{

	private ConfigurationService service;

	@Autowired(required=true)
	public FlexConfigurationService(@Qualifier("jdbcConfigurationService")ConfigurationService service) {
		this.service = service;
	}

	@Override
	public ConfigurationVO find() {
		return this.service.find();
	}

	@Override
	@Secured("ROLE_ADMIN")
	public void edit(ConfigurationVO configurationVO) {
		this.service.edit(configurationVO);
	}

	@Override
	@Secured("ROLE_ADMIN")
	public void testMail(ConfigurationVO configurationVO) {
		this.service.testMail(configurationVO);
	}

	@Override
	public boolean testLicense(String license) {
		return service.testLicense(license);
	}

}
