package org.mxhero.console.backend.service.flex;

import java.util.Collection;

import org.mxhero.console.backend.service.TrafficReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service("trafficReportService")
@RemotingDestination(channels = { "flex-amf" })
public class FlexTrafficReportService implements TrafficReportService{

	private TrafficReportService service;

	@Autowired(required=true)
	public FlexTrafficReportService(@Qualifier("jdbcTrafficReportService")TrafficReportService service) {
		this.service = service;
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public Collection getIncomming(String domain, long since, String offset) {
		return service.getIncomming(domain, since, offset);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public Collection getIncommingByDay(String domain, long day) {
		return service.getIncommingByDay(domain, day);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public Collection getOutgoing(String domain, long since, String offset) {
		return service.getOutgoing(domain, since, offset);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public Collection getOutgoingByDay(String domain, long day) {
		return service.getOutgoingByDay(domain, day);
	}

}
