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

package org.mxhero.console.backend.service;

import java.util.Collection;

import org.mxhero.console.backend.vo.RecordVO;

public interface ThreatsReportService {

	Collection getSpamHits(String domain, long since, String offset);
	
	Collection getSpamHitsDay (String domain, long since);
	
	Collection getVirusHits(String domain, long since, String offset);
	
	Collection getVirusHitsDay(String domain, long since);
	
	Collection<RecordVO> getSpamEmails(String domain, long since, long until);
	
	Collection<RecordVO> getVirusEmails(String domain, long since, long until);
	
}
