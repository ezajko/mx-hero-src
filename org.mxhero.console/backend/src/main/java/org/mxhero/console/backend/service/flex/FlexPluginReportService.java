package org.mxhero.console.backend.service.flex;

import java.util.Collection;
import java.util.List;

import org.mxhero.console.backend.service.PluginReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service("pluginReportService")
@RemotingDestination(channels = { "flex-amf" })
public class FlexPluginReportService implements PluginReportService{

	private PluginReportService service;
	
	@Autowired(required=true)
	public FlexPluginReportService(@Qualifier("jdbcPluginReportService")PluginReportService service) {
		this.service = service;
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public Collection getResult(String queryString, List params) {
		return service.getResult(queryString, params);
	}

}
