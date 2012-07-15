package org.mxhero.console.backend.service;

import org.mxhero.console.backend.vo.ConfigurationVO;

public interface ConfigurationService {

	ConfigurationVO find();
	
	void edit(ConfigurationVO configurationVO);
	
	void testMail(ConfigurationVO configurationVO);
	
	boolean testLicense(String license);
}
