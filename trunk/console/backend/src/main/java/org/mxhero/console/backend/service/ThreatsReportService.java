package org.mxhero.console.backend.service;

import java.util.Calendar;
import java.util.Collection;

import org.mxhero.console.backend.vo.RecordVO;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

public interface ThreatsReportService {

	@Secured("ROLE_DOMAIN_ADMIN")
	@Transactional(readOnly=true)
	Collection getSpamHits(String domain, long since, String offset);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	@Transactional(readOnly=true)
	Collection getSpamHitsDay (String domain, long since);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	@Transactional(readOnly=true)
	Collection getVirusHits(String domain, long since, String offset);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	@Transactional(readOnly=true)
	Collection getVirusHitsDay(String domain, long since);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	@Transactional(readOnly=true)
	Collection<RecordVO> getSpamEmails(String domain, long since, long until);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	@Transactional(readOnly=true)
	Collection<RecordVO> getVirusEmails(String domain, long since, long until);
	
}
