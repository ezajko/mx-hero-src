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
package org.mxhero.console.reports.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.domain.FeatureRuleDirection;
	import org.mxhero.console.reports.application.event.GetTopTenOutgoingRecipientsByDayEvent;

	public class GetTopTenOutgoingRecipientsByDayCommand
	{
		[Inject(id="customReportService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:GetTopTenOutgoingRecipientsByDayEvent):AsyncToken
		{
			var sinceDay:Date = new Date();
			sinceDay.setTime(event.day.getTime());
			sinceDay.setHours(0,0,0,0);
			var untilDay:Date = new Date();
			untilDay.setTime(event.day.getTime()+24*60*60*1000);
			untilDay.setHours(0,0,0,0);
			var to:FeatureRuleDirection = new FeatureRuleDirection();
			var from:FeatureRuleDirection = new FeatureRuleDirection();
			from.directionType=FeatureRuleDirection.ANYONE;
			from.freeValue=FeatureRuleDirection.ANYONE;
			
			if(event.domainId==null){
				//if we get here we are at the admin view
				if(event.onlyDomain){
					//only recipients that are allocated in one domain in the platform
					to.directionType=FeatureRuleDirection.ALLDOMAINS;
					to.freeValue=FeatureRuleDirection.ALLDOMAINS;
				}else{
					//any sender o recipients is the same thing
					to.directionType=FeatureRuleDirection.ANYONE;
					to.freeValue=FeatureRuleDirection.ANYONE;
				}
			}else{
				//if we get here we are in the domain view
				if(event.onlyDomain){
					//only recipients from this domain
					to.domain=event.domainId;
					to.freeValue=event.domainId;
					to.directionType=FeatureRuleDirection.DOMAIN;
				}else{
					//recipients can be from any domain but senders must be in the domain
					to.directionType=FeatureRuleDirection.ANYONE;
					to.freeValue=FeatureRuleDirection.ANYONE;
					from.domain=event.domainId;
					from.freeValue=event.domainId;
					from.directionType=FeatureRuleDirection.DOMAIN;
				}
			}
			
			return service.getTopTenRecipients(from,to,sinceDay.getTime(),untilDay.getTime());		
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}