package org.mxhero.console.backend.service;

import org.mxhero.console.backend.vo.ActivityDataVO;
import org.mxhero.console.backend.vo.MessagesCompositionVO;
import org.mxhero.console.backend.vo.MxHeroDataVO;


public interface HomeReportService {

	MxHeroDataVO getMxHeroData(String domainId);
	
	MessagesCompositionVO getMessagesCompositionData(long since, String domainId);

	ActivityDataVO getActivity(long since, String domainId);
	
	ActivityDataVO getActivityByHour(long since, String domainId);
}
