package org.mxhero.console.backend.service;

import java.util.Collection;
import java.util.List;

import org.mxhero.console.backend.vo.FeatureRuleDirectionVO;
import org.mxhero.console.backend.vo.RecordStatVO;
import org.mxhero.console.backend.vo.RecordVO;

public interface CustomReportService {

	Collection getTopTenSenders(FeatureRuleDirectionVO from, FeatureRuleDirectionVO to, long since, long until);

	Collection getTopTenRecipients(FeatureRuleDirectionVO from, FeatureRuleDirectionVO to, long since, long until);
	
	Collection<RecordVO> getEmails(FeatureRuleDirectionVO from, FeatureRuleDirectionVO to, long since, long until);
	
	List<RecordStatVO> getStats(Long insertDate, Long sequence);
}
