package org.mxhero.console.backend.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

public interface PluginReportService {

	@Secured("ROLE_DOMAIN_ADMIN")
	@Transactional(readOnly=true)
	Collection getResult (String queryString, List params);
	
	
}
