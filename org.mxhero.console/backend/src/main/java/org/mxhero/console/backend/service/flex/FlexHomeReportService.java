package org.mxhero.console.backend.service.flex;

import org.mxhero.console.backend.service.HomeReportService;
import org.mxhero.console.backend.vo.ActivityDataVO;
import org.mxhero.console.backend.vo.MessagesCompositionVO;
import org.mxhero.console.backend.vo.MxHeroDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service("homeReportService")
@RemotingDestination(channels = { "flex-amf" })
public class FlexHomeReportService implements HomeReportService{

	private HomeReportService service;
	
	@Autowired(required=true)
	public FlexHomeReportService(@Qualifier("jdbcHomeReportService")HomeReportService service) {
		super();
		this.service = service;
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public MxHeroDataVO getMxHeroData(String domainId) {
		return service.getMxHeroData(domainId);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public MessagesCompositionVO getMessagesCompositionData(long since,
			String domainId) {
		return service.getMessagesCompositionData(since, domainId);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public ActivityDataVO getActivity(long since, String domainId) {
		return service.getActivity(since, domainId);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public ActivityDataVO getActivityByHour(long since, String domainId) {
		return service.getActivityByHour(since, domainId);
	}

}
