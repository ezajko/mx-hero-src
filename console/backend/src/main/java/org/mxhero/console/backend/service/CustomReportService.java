package org.mxhero.console.backend.service;

import java.util.Calendar;
import java.util.Collection;

import org.mxhero.console.backend.vo.FeatureRuleDirectionVO;
import org.mxhero.console.backend.vo.RecordVO;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

public interface CustomReportService {

	@Secured("ROLE_DOMAIN_ADMIN")
	@Transactional(readOnly=true)
	Collection getTopTenSenders(FeatureRuleDirectionVO from, FeatureRuleDirectionVO to, long since, long until);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	@Transactional(readOnly=true)
	Collection getTopTenRecipients(FeatureRuleDirectionVO from, FeatureRuleDirectionVO to, long since, long until);

	@Secured("ROLE_DOMAIN_ADMIN")
	@Transactional(readOnly=true)
	Collection<RecordVO> getEmails(FeatureRuleDirectionVO from, FeatureRuleDirectionVO to, long since, long until);
	
}
