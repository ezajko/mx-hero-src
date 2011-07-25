package org.mxhero.console.backend.service;

import java.util.Collection;

import org.mxhero.console.backend.vo.RecordVO;
import org.springframework.security.access.annotation.Secured;

public interface ThreatsReportService {

	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getSpamHits(String domain, long since, String offset);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getSpamHitsDay (String domain, long since);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getVirusHits(String domain, long since, String offset);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getVirusHitsDay(String domain, long since);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	Collection<RecordVO> getSpamEmails(String domain, long since, long until);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	Collection<RecordVO> getVirusEmails(String domain, long since, long until);
	
}
