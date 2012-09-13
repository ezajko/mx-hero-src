/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
