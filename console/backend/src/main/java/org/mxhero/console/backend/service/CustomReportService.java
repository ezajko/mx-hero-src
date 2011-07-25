package org.mxhero.console.backend.service;

import java.util.Collection;

import org.mxhero.console.backend.vo.FeatureRuleDirectionVO;
import org.mxhero.console.backend.vo.RecordVO;
import org.springframework.security.access.annotation.Secured;

public interface CustomReportService {

	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getTopTenSenders(FeatureRuleDirectionVO from, FeatureRuleDirectionVO to, long since, long until);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	Collection getTopTenRecipients(FeatureRuleDirectionVO from, FeatureRuleDirectionVO to, long since, long until);

	@Secured("ROLE_DOMAIN_ADMIN")
	Collection<RecordVO> getEmails(FeatureRuleDirectionVO from, FeatureRuleDirectionVO to, long since, long until);
	
}
