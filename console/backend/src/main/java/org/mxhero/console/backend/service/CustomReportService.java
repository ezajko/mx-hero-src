package org.mxhero.console.backend.service;

import java.util.Calendar;
import java.util.Collection;

import org.mxhero.console.backend.vo.FeatureRuleDirectionVO;
import org.mxhero.console.backend.vo.PageVO;
import org.mxhero.console.backend.vo.RecordVO;

public interface CustomReportService {

	Collection getTopTenSenders(FeatureRuleDirectionVO from, FeatureRuleDirectionVO to, Calendar since, Calendar until);
	
	Collection getTopTenRecipients(FeatureRuleDirectionVO from, FeatureRuleDirectionVO to, Calendar since, Calendar until);

	Collection<RecordVO> getEmails(FeatureRuleDirectionVO from, FeatureRuleDirectionVO to, Calendar since, Calendar until);
	
}
