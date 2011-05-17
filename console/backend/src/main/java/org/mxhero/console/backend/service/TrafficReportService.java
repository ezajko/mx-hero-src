package org.mxhero.console.backend.service;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

public interface TrafficReportService {


	@Secured("ROLE_DOMAIN_ADMIN")
	@Transactional(readOnly=true)
	Collection getIncomming(String domain, long since, String offset);

	@Secured("ROLE_DOMAIN_ADMIN")
	@Transactional(readOnly=true)
	Collection getIncommingByDay(String domain, long day);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	@Transactional(readOnly=true)
	Collection getTopTenIncomingSenders(String domain, long since);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	@Transactional(readOnly=true)
	Collection getTopTenIncomingSendersByDay(String domain, long day);

	@Secured("ROLE_DOMAIN_ADMIN")
	@Transactional(readOnly=true)
	Collection getOutgoing(String domain, long since, String offset);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	@Transactional(readOnly=true)
	Collection getOutgoingByDay(String domain, long day);

	@Secured("ROLE_DOMAIN_ADMIN")
	@Transactional(readOnly=true)
	Collection getTopTenOutgoingRecipients(String domain,long since);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	@Transactional(readOnly=true)
	Collection getTopTenOutgoingRecipientsByDay(String domain, long day);
}
