package org.mxhero.console.backend.service;

import org.mxhero.console.backend.vo.ActivityDataVO;
import org.mxhero.console.backend.vo.MessagesCompositionVO;
import org.mxhero.console.backend.vo.MxHeroDataVO;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;


public interface HomeReportService {

	@Secured("ROLE_DOMAIN_ADMIN")
	MxHeroDataVO getMxHeroData(String domainId);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	MessagesCompositionVO getMessagesCompositionData(long since, String domainId);

	@Secured("ROLE_DOMAIN_ADMIN")
	ActivityDataVO getActivity(long since, String domainId);
}
