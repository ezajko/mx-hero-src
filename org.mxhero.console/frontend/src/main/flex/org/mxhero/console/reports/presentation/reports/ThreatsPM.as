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
	
	import org.mxhero.console.commons.infrastructure.TimeUtils;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.reports.application.ReportsDestinations;
	import org.mxhero.console.reports.application.event.GetSpamEmailsEvent;
	import org.mxhero.console.reports.application.event.GetSpamHitsDayEvent;
	import org.mxhero.console.reports.application.event.GetSpamHitsEvent;
	import org.mxhero.console.reports.application.event.GetVirusEmailsEvent;
	import org.mxhero.console.reports.application.event.GetVirusHitsDayEvent;
	import org.mxhero.console.reports.application.event.GetVirusHitsEvent;
	import org.mxhero.console.reports.presentation.ReportsViewPM;

	[Landmark(name="main.dashboard.reports.threats")]
	public class ThreatsPM
	{
		[Inject]
		[Bindable]
		public var parentModel:ReportsViewPM;
		
		[Inject]
		[Bindable]
		public var context:ApplicationContext;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Bindable]
		public var stateVirus:String = "zoom_out";
		[Bindable]
		public var stateSpam:String = "zoom_out";
		
		[Bindable]
		public var spamHists:ArrayCollection;
		[Bindable]
		public var spamMails:ArrayCollection;
		[Bindable]
		public var virusHists:ArrayCollection;
		[Bindable]
		public var virusMails:ArrayCollection;
		
		private static const DAYSBEFORE:Number = 14*24*60*60*1000; 
		private static const PLUSDAY:Number = 24*60*60*1000; 
		
		[Bindable]
		public var virusUpdating:Boolean=false;
		[Bindable]
		public var spamUpdating:Boolean=false;
		[Bindable]
		public var sinceDate:Date=new Date();
		[Bindable]
		public var untilDate:Date=new Date();
		[Bindable]
		public var since24Hs:Date = new Date();
		
		public function goBack():void{
			parentModel.navigateTo(ReportsDestinations.LIST);
		}
		
		[Enter(time="every")]
		public function every():void{
			spamHists=null;
			spamMails=null;
			virusHists=null;
			virusMails=null;
			untilDate.setTime(new Date());
			untilDate.setHours(0,0,0,0);
			untilDate.setTime(untilDate.getTime()+PLUSDAY);
			sinceDate.setTime(new Date().getTime()-DAYSBEFORE);
			sinceDate.setHours(0,0,0,0);
			since24Hs = new Date();
			getVirus();
			getSpam();
		}
		
		public function getVirus():void{
			since24Hs=new Date();
			var sinceMails:Date=new Date();
			sinceMails.setHours(0,0,0,0);
			this.virusHists=null;
			this.virusMails=null;
			var domain:String = null;
			
			if(context.selectedDomain!=null){
				domain = context.selectedDomain.domain;
			}
			dispatcher( new GetVirusHitsEvent(domain,sinceDate) );
			dispatcher( new GetVirusEmailsEvent(domain,sinceMails,untilDate));
			stateVirus = "zoom_out";
			this.virusUpdating=true;
		}
		
		public function getSpam():void{
			since24Hs=new Date();
			var sinceMails:Date=new Date();
			sinceMails.setHours(0,0,0,0);
			spamHists=null;
			spamMails=null;
			var domain:String = null;
			
			if(context.selectedDomain!=null){
				domain = context.selectedDomain.domain;
			}
			dispatcher( new GetSpamHitsEvent(domain,sinceDate) );
			dispatcher( new GetSpamEmailsEvent(domain,sinceMails,untilDate));
			stateSpam = "zoom_out";
			this.spamUpdating=true;
		}
		
		public function getVirusByDay(day:Date):void{
			var sinceDay:Date = new Date();
			var untilDay:Date = new Date();
			var domain:String = null;
			since24Hs=day;
			sinceDay.setTime(day.getTime());
			sinceDay.setHours(0,0,0,0);
			untilDay.setTime(day.getTime()+PLUSDAY);
			untilDay.setHours(0,0,0,0);
			
			this.virusHists=null;
			this.virusMails=null;
			if(context.selectedDomain!=null){
				domain = context.selectedDomain.domain;
			}
			dispatcher( new GetVirusHitsDayEvent(domain,sinceDay) );
			dispatcher( new GetVirusEmailsEvent(domain,sinceDay,untilDay));
			this.virusUpdating=true;
			stateVirus="zoom_in"
		}
		
		public function getSpamByDay(day:Date):void{
			var sinceDay:Date = new Date();
			var untilDay:Date = new Date();
			var domain:String = null;
			since24Hs=day;
			sinceDay.setTime(day.getTime());
			sinceDay.setHours(0,0,0,0);
			untilDay.setTime(day.getTime()+PLUSDAY);
			untilDay.setHours(0,0,0,0);
			
			spamHists=null;
			spamMails=null;
			
			if(context.selectedDomain!=null){
				domain = context.selectedDomain.domain;
			}
			dispatcher( new GetSpamHitsDayEvent(domain,sinceDay) );
			dispatcher( new GetSpamEmailsEvent(domain,sinceDay,untilDay));
			this.spamUpdating=true;
			stateSpam="zoom_in"
		}
		
		[CommandResult]
		public function getSpamHitsResult(result:*,event:GetSpamHitsEvent):void{
			this.spamHists=translateData(result);
			this.spamUpdating=false;
		}
		
		[CommandError]
		public function getSpamHitsError(fault:*,event:GetSpamHitsEvent):void{
			this.spamHists=new ArrayCollection();
			spamUpdating=false;
		}
		
		[CommandResult]
		public function getSpamHitsDayResult(result:*,event:GetSpamHitsDayEvent):void{
			this.spamHists=translateDataByDay(result);
			this.spamUpdating=false;
		}
		
		[CommandError]
		public function getSpamHitsDayError(fault:*,event:GetSpamHitsDayEvent):void{
			this.spamHists=new ArrayCollection();
			spamUpdating=false;
		}
		
		
		[CommandResult]
		public function getVirusHitsResult(result:*,event:GetVirusHitsEvent):void{
			this.virusHists=translateData(result);
			this.virusUpdating=false;
		}
		
		[CommandError]
		public function getVirusHitsError(fault:*,event:GetVirusHitsEvent):void{
			this.virusHists=new ArrayCollection();
			virusUpdating=false;
		}
		
		[CommandResult]
		public function getVirusHitsDayResult(result:*,event:GetVirusHitsDayEvent):void{
			this.virusHists=translateDataByDay(result);
			this.virusUpdating=false;
		}
		
		[CommandError]
		public function getVirusHitsDayError(fault:*,event:GetVirusHitsDayEvent):void{
			this.virusHists=new ArrayCollection();
			virusUpdating=false;
		}
		
		
		[CommandResult]
		public function getVirusMailsResult(result:*,event:GetVirusEmailsEvent):void{
			this.virusMails=result;
			this.virusUpdating=false;
		}
		
		[CommandError]
		public function getVirusMailsError(fault:*,event:GetVirusEmailsEvent):void{
			this.virusMails=new ArrayCollection();
			virusUpdating=false;
		}
		
		[CommandResult]
		public function getSpamMailsResult(result:*,event:GetSpamEmailsEvent):void{
			this.spamMails=result;
			this.spamUpdating=false;
		}
		
		[CommandError]
		public function getSpamMailsError(fault:*,event:GetSpamEmailsEvent):void{
			this.spamMails=new ArrayCollection();
			spamUpdating=false;
		}
		
		private function translateDataByDay(result:*):ArrayCollection{
			var newData:ArrayCollection = new ArrayCollection();
			if(result!=null){
				if(result is Array || result is ArrayCollection){
					for each(var object:Object in result){
						var day:Date = new Date((object.date as Date).getTime());
						day.setUTCHours(object.hours);
						day.setTime(day.getTime()-day.getTimezoneOffset()*60*1000);
						newData.addItem({Qty: object.count, Date:day});
					}
				}
			}
			return newData;
		}
		
		private function translateData(result:*):ArrayCollection{
			var newData:ArrayCollection = new ArrayCollection();
			if(result!=null){
				if(result is Array || result is ArrayCollection){
					for each(var object:Object in result){
						var day:Date = object.date;
						day.setTime(day.getTime()+day.getTimezoneOffset()*60*1000);
						newData.addItem({Qty: object.count, Date:day});
					}
				}
			}
			return newData;
		}
	}
}