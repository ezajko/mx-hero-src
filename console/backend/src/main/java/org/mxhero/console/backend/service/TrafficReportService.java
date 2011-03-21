package org.mxhero.console.backend.service;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.security.access.annotation.Secured;

public interface TrafficReportService {


	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getIncomming(String domain);

	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getIncommingByDay(String domain, Calendar day);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getTopTenIncomingSenders(String domain);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getTopTenIncomingSendersByDay(String domain, Calendar day);

	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getOutgoing(String domain);

	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getTopTenOutgoingRecipients(String domain);
}
