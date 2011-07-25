package org.mxhero.console.backend.service;

import java.util.Collection;

import org.springframework.security.access.annotation.Secured;

public interface TrafficReportService {


	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getIncomming(String domain, long since, String offset);

	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getIncommingByDay(String domain, long day);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getTopTenIncomingSenders(String domain, long since, boolean domainOnly);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getTopTenIncomingSendersByDay(String domain, long day, boolean domainOnly);

	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getOutgoing(String domain, long since, String offset);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getOutgoingByDay(String domain, long day);

	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getTopTenOutgoingRecipients(String domain,long since, boolean domainOnly);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getTopTenOutgoingRecipientsByDay(String domain, long day, boolean domainOnly);
}
