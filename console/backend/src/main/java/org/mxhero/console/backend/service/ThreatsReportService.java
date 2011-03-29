package org.mxhero.console.backend.service;

import java.util.Calendar;
import java.util.Collection;

import org.mxhero.console.backend.vo.RecordVO;
import org.springframework.security.access.annotation.Secured;

public interface ThreatsReportService {

	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getSpamHits(String domain, Calendar since);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getSpamHitsDay (String domain, Calendar since);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getVirusHits(String domain, Calendar since);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getVirusHitsDay(String domain, Calendar since);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	Collection<RecordVO> getSpamEmails(String domain, Calendar since, Calendar until);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	Collection<RecordVO> getVirusEmails(String domain, Calendar since, Calendar until);
	
}
