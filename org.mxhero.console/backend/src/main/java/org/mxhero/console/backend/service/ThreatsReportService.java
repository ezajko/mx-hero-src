package org.mxhero.console.backend.service;

import java.util.Collection;

import org.mxhero.console.backend.vo.RecordVO;

public interface ThreatsReportService {

	Collection getSpamHits(String domain, long since, String offset);
	
	Collection getSpamHitsDay (String domain, long since);
	
	Collection getVirusHits(String domain, long since, String offset);
	
	Collection getVirusHitsDay(String domain, long since);
	
	Collection<RecordVO> getSpamEmails(String domain, long since, long until);
	
	Collection<RecordVO> getVirusEmails(String domain, long since, long until);
	
}
