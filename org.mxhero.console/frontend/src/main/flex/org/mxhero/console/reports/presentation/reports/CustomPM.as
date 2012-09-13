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
package org.mxhero.console.reports.presentation.reports
{
	import mx.collections.ArrayCollection;
	
	import org.mxhero.console.frontend.application.event.GetAccountsEvent;
	import org.mxhero.console.frontend.application.event.GetDomainGroupsEvent;
	import org.mxhero.console.frontend.application.event.GetDomainsEvent;
	import org.mxhero.console.frontend.application.event.LogoutEvent;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.FeatureRuleDirection;
	import org.mxhero.console.reports.application.ReportsDestinations;
	import org.mxhero.console.reports.application.event.GetEmailsEvent;
	import org.mxhero.console.reports.application.event.GetTopTenRecipientsEvent;
	import org.mxhero.console.reports.application.event.GetTopTenSendersEvent;
	import org.mxhero.console.reports.presentation.ReportsViewPM;
	
	[Landmark(name="main.dashboard.reports.custom")]
	public class CustomPM
	{
		
		public static var refresh:Function;
		
		[Bindable]
		public var updatingEmails:Boolean=false;
		
		[Bindable]
		public var emails:ArrayCollection;
		
		[Bindable]
		public var topTenSenders:ArrayCollection;
		
		[Bindable]
		public var topTenRecipients:ArrayCollection;
		
		[Inject]
		[Bindable]
		public var parentModel:ReportsViewPM;
		
		[Inject]
		[Bindable]
		public var context:ApplicationContext;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Bindable]
		public var fromDirection:FeatureRuleDirection=new FeatureRuleDirection();
		[Bindable]
		public var toDirection:FeatureRuleDirection=new FeatureRuleDirection();
		
		[Enter(time="every")]
		public function every():void{
			fromDirection=new FeatureRuleDirection();
			toDirection=new FeatureRuleDirection();
			if(refresh!=null){
				refresh();
			}
			emails=null;
			topTenSenders=null;
			topTenRecipients=null;

			if(context.selectedDomain==null){
				dispatcher(new GetDomainsEvent());
				dispatcher(new GetAccountsEvent());
			} else {
				var newDomains:ArrayCollection = new ArrayCollection();
				newDomains.addItem(context.selectedDomain);
				context.domains=newDomains;
				dispatcher(new GetAccountsEvent(context.selectedDomain.domain));
				dispatcher(new GetDomainGroupsEvent(context.selectedDomain.domain));
			}
		}
		
		public function filterAccount(accountFilter:String):void{
			if(context.selectedDomain==null){
				dispatcher(new GetAccountsEvent(null,accountFilter));
			}else{
				dispatcher(new GetAccountsEvent(context.selectedDomain.domain,accountFilter));
			}
		}
		
		public function filterDomain(domainFilter:String):void{
			if(context.selectedDomain==null){
				dispatcher(new GetDomainsEvent(domainFilter));
			}
		}
		
		public function goBack():void{
			fromDirection=new FeatureRuleDirection();
			toDirection=new FeatureRuleDirection();
			parentModel.navigateTo(ReportsDestinations.LIST);
		}
		
		public function filter(since:Date, until:Date):void{
			var sinceDate:Date = new Date();
			var untilDate:Date = new Date();

			sinceDate.setTime(since.getTime());
			sinceDate.setHours(0,0,0,0);
			untilDate.setTime(until.getTime()+24*60*60*1000);
			untilDate.setHours(0,0,0,0);
			
			dispatcher(new GetEmailsEvent(this.fromDirection,this.toDirection,sinceDate.getTime(),untilDate.getTime()));
			dispatcher(new GetTopTenSendersEvent(this.fromDirection,this.toDirection,sinceDate.getTime(),untilDate.getTime()));
			dispatcher(new GetTopTenRecipientsEvent(this.fromDirection,this.toDirection,sinceDate.getTime(),untilDate.getTime()));
			updatingEmails=true;
		}
		
		[CommandResult]
		public function getEmailsResult(result:*,event:GetEmailsEvent):void{
			this.emails=result;
			updatingEmails=false;
		}
		
		[CommandError]
		public function getEmailsError(fault:*,event:GetEmailsEvent):void{
			this.emails=null;
			updatingEmails=false;
		}
		
		[CommandResult]
		public function getTopTenSendersResult(result:*,event:GetTopTenSendersEvent):void{
			this.topTenSenders=translateMail(result);
			updatingEmails=false;
		}
		
		[CommandError]
		public function getTopTenSendersError(fault:*,event:GetTopTenSendersEvent):void{
			this.topTenSenders=null;
			updatingEmails=false;
		}

		[CommandResult]
		public function getTopTenRecipientsResult(result:*,event:GetTopTenRecipientsEvent):void{
			this.topTenRecipients=translateMail(result);
			updatingEmails=false;
		}
		
		[CommandError]
		public function getTopTenRecipientsError(fault:*,event:GetTopTenRecipientsEvent):void{
			this.topTenRecipients=null;
			updatingEmails=false;
		}
		
		private function translateMail(result:*):ArrayCollection{
			var newData:ArrayCollection = new ArrayCollection();
			if(result!=null){
				if(result is Array || result is ArrayCollection){
					for each(var object:Object in result){
						newData.addItem({Qty: object.count, Email:object.label});
					}
				}
				newData.source = newData.source.reverse();
			}
			return newData;
		}
	}
}