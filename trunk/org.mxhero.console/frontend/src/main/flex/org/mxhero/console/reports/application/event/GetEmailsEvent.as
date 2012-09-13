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
package org.mxhero.console.reports.application.event
{
	import org.mxhero.console.frontend.domain.FeatureRuleDirection;

	public class GetEmailsEvent
	{
		public var from:FeatureRuleDirection;
		public var to:FeatureRuleDirection;
		public var since:Number;
		public var until:Number;
		
		public function GetEmailsEvent(from:FeatureRuleDirection
									    ,to:FeatureRuleDirection
										,since:Number
										,until:Number)
		{
			this.from=from;
			this.to=to;
			this.since=since;
			this.until=until;
		}
	}
}