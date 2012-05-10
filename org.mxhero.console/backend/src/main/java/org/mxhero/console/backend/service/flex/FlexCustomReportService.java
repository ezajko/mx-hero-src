package org.mxhero.console.backend.service.flex;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.mxhero.console.backend.service.CustomReportService;
import org.mxhero.console.backend.vo.FeatureRuleDirectionVO;
import org.mxhero.console.backend.vo.RecordStatVO;
import org.mxhero.console.backend.vo.RecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service("customReportService")
@RemotingDestination(channels = { "flex-amf" })
public class FlexCustomReportService implements CustomReportService{

	private CustomReportService service;
	
	@Autowired(required=true)
	public FlexCustomReportService(@Qualifier("jdbcCustomReportService")CustomReportService service) {
		this.service = service;
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public Collection getTopTenSenders(FeatureRuleDirectionVO from,
			FeatureRuleDirectionVO to, long since, long until) {
		return service.getTopTenSenders(from, to, since, until);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public Collection getTopTenRecipients(FeatureRuleDirectionVO from,
			FeatureRuleDirectionVO to, long since, long until) {
		return service.getTopTenRecipients(from, to, since, until);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public Collection<RecordVO> getEmails(FeatureRuleDirectionVO from,
			FeatureRuleDirectionVO to, long since, long until) {
		return service.getEmails(from, to, since, until);
	}

	@Override
	public List<RecordStatVO> getStats(Long insertDate, Long sequence) {
		return service.getStats(insertDate, sequence);
	}

}
