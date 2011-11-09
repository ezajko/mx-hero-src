package org.mxhero.console.backend.service;

import org.mxhero.console.backend.vo.ConfigurationVO;
import org.springframework.security.access.annotation.Secured;

public interface ConfigurationService {

	ConfigurationVO find();
	
	@Secured("ROLE_ADMIN")
	void edit(ConfigurationVO configurationVO);
	
	@Secured("ROLE_ADMIN")
	void testMail(ConfigurationVO configurationVO);
}
