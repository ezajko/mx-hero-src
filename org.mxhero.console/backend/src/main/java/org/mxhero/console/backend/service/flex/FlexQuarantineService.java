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

import org.mxhero.console.backend.service.QuarantineService;
import org.mxhero.console.backend.vo.QuarantineVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;

@Service("quarantineService")
@RemotingDestination(channels = { "flex-amf" })
public class FlexQuarantineService implements QuarantineService{

	private QuarantineService service;
	
	@Autowired(required=true)
	public FlexQuarantineService(@Qualifier("jdbcQuarantineService")QuarantineService service) {
		this.service = service;
	}

	@Override
	public void save(QuarantineVO quarantine) {
		service.save(quarantine);
	}

	@Override
	public QuarantineVO read(String domain) {
		return service.read(domain);
	}

}
