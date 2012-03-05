package org.mxhero.console.backend.service.flex;

import java.util.Collection;

import org.mxhero.console.backend.service.ThreatsReportService;
import org.mxhero.console.backend.vo.RecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service("threatsReportService")
@RemotingDestination(channels = { "flex-amf" })
public class FlexThreatsReportService implements ThreatsReportService{

	private ThreatsReportService service;

	@Autowired(required=true)
	public FlexThreatsReportService(@Qualifier("jdbcThreatsReportService")ThreatsReportService service) {
		this.service = service;
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public Collection getSpamHits(String domain, long since, String offset) {
		return service.getSpamHits(domain, since, offset);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public Collection getSpamHitsDay(String domain, long since) {
		return service.getSpamHitsDay(domain, since);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public Collection getVirusHits(String domain, long since, String offset) {
		return service.getVirusHits(domain, since, offset);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public Collection getVirusHitsDay(String domain, long since) {
		return service.getVirusHitsDay(domain, since);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public Collection<RecordVO> getSpamEmails(String domain, long since,
			long until) {
		return service.getSpamEmails(domain, since, until);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public Collection<RecordVO> getVirusEmails(String domain, long since,
			long until) {
		return service.getVirusEmails(domain, since, until);
	}
	
}
