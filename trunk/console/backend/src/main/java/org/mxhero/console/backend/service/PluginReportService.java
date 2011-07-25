package org.mxhero.console.backend.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.access.annotation.Secured;

public interface PluginReportService {

	Integer MAX_RESULT = 1000;
	
	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getResult (String queryString, List params);
	
	
}
