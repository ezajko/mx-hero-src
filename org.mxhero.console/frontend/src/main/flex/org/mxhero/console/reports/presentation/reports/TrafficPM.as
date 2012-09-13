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
	
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.reports.application.ReportsDestinations;
	import org.mxhero.console.reports.application.event.GetIncommingByDayEvent;
	import org.mxhero.console.reports.application.event.GetIncommingEvent;
	import org.mxhero.console.reports.application.event.GetOutgoingByDayEvent;
	import org.mxhero.console.reports.application.event.GetOutgoingEvent;
	import org.mxhero.console.reports.application.event.GetTopTenIncommingSendersByDayEvent;
	import org.mxhero.console.reports.application.event.GetTopTenOutgoingRecipientsByDayEvent;
	import org.mxhero.console.reports.presentation.ReportsViewPM;

	[Landmark(name="main.dashboard.reports.traffic")]
	public class TrafficPM
	{
		[Bindable]
		public var incommingData:ArrayCollection=new ArrayCollection();
		[Bindable]
		public var updatingIncoming:Boolean = false;
		[Bindable]
		public var incommingSendersData:ArrayCollection=new ArrayCollection();
		[Bindable]
		public var updatingIncomingSenders:Boolean = false;		
		[Bindable]
		public var outgoingData:ArrayCollection=new ArrayCollection();
		[Bindable]
		public var updatingOutgoing:Boolean = false;
		[Bindable]
		public var outgoingRecipientsData:ArrayCollection=new ArrayCollection();
		[Bindable]
		public var updatingOutgoingRecipients:Boolean = false;
		
		private static const IN_DEFAULT:String = "zoom_out";
		private static const OUT_DEFAULT:String = "zoom_out";
		private static const IN_DAY:String = "zoom_in";
		private static const OUT_DAY:String = "zoom_in";
		
		[Bindable]
		public var onlyDomain:Boolean=false;
		
		[Bindable]
		public var stateIncomming:String = IN_DEFAULT;
		
		[Bindable]
		public var stateOutgoing:String = OUT_DEFAULT;
		
		[Bindable]
		public var sinceDate:Date=new Date();;
		
		[Bindable]
		public var untilDate:Date=new Date();;
		
		[Bindable]
		public var since24Hs:Date = new Date();
		
		private static const DAYSBEFORE:Number = 14*24*60*60*1000; 
		private static const PLUSDAY:Number = 24*60*60*1000; 
		
		[Inject]
		[Bindable]
		public var parentModel:ReportsViewPM;
		
		[Inject]
		[Bindable]
		public var context:ApplicationContext;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function goBack():void{
			parentModel.navigateTo(ReportsDestinations.LIST);
		}
		
		[Enter(time="every")]
		public function every():void{
			untilDate.setTime(new Date());
			untilDate.setHours(0,0,0,0);
			untilDate.setTime(untilDate.getTime()+PLUSDAY);
			sinceDate.setTime(new Date().getTime()-DAYSBEFORE);
			sinceDate.setHours(0,0,0,0);
			since24Hs=new Date();
			getIncomming();
			getOutgoing();
		}
		
		public function getIncomming():void{
			since24Hs=new Date();
			if(context.selectedDomain!=null){
				dispatcher(new GetIncommingEvent(context.selectedDomain.domain,sinceDate));
				dispatcher(new GetTopTenIncommingSendersByDayEvent(context.selectedDomain.domain,since24Hs,this.onlyDomain));
			}else{
				dispatcher(new GetIncommingEvent(null,sinceDate));
				dispatcher(new GetTopTenIncommingSendersByDayEvent(null,since24Hs,this.onlyDomain));
			}
			updatingIncoming=true;
			updatingIncomingSenders=true;
		}
		
		public function getIncommingByDay(day:Date):void{
			var sinceDay:Date = new Date();
			sinceDay.setTime(day.getTime());
			sinceDay.setHours(0,0,0,0);
			since24Hs=day;
			if(context.selectedDomain!=null){
				dispatcher(new GetIncommingByDayEvent(context.selectedDomain.domain,sinceDay));
				dispatcher(new GetTopTenIncommingSendersByDayEvent(context.selectedDomain.domain,sinceDay,this.onlyDomain));
			}else{
				dispatcher(new GetIncommingByDayEvent(null,sinceDay));
				dispatcher(new GetTopTenIncommingSendersByDayEvent(null,sinceDay,this.onlyDomain));
			}
			updatingIncoming=true;
			updatingIncomingSenders=true;
		}
		
		public function getOutgoing():void{
			since24Hs=new Date();
			if(context.selectedDomain!=null){
				dispatcher(new GetOutgoingEvent(context.selectedDomain.domain,sinceDate));
				dispatcher(new GetTopTenOutgoingRecipientsByDayEvent(context.selectedDomain.domain,since24Hs,this.onlyDomain));
			}else{
				dispatcher(new GetOutgoingEvent(null,sinceDate));
				dispatcher(new GetTopTenOutgoingRecipientsByDayEvent(null,since24Hs,this.onlyDomain));
			}
			updatingOutgoing=true;
			updatingOutgoingRecipients=true;
		}
		
		public function getOutgoingByDay(day:Date):void{
			var sinceDay:Date = new Date();
			sinceDay.setTime(day.getTime());
			sinceDay.setHours(0,0,0,0);
			since24Hs=day;
			if(context.selectedDomain!=null){
				dispatcher(new GetOutgoingByDayEvent(context.selectedDomain.domain,sinceDay));
				dispatcher(new GetTopTenOutgoingRecipientsByDayEvent(context.selectedDomain.domain,sinceDay,this.onlyDomain));
			}else{
				dispatcher(new GetOutgoingByDayEvent(null,sinceDay));
				dispatcher(new GetTopTenOutgoingRecipientsByDayEvent(null,sinceDay,this.onlyDomain));
			}
			updatingOutgoing=true;
			updatingOutgoingRecipients=true;
		}
		
		[CommandResult]
		public function getIncommingResult(result:*,event:GetIncommingEvent):void{
			this.incommingData.removeAll();
			stateIncomming = IN_DEFAULT;
			this.incommingData.addAll(translateData(result));
			updatingIncoming=false;
		}
		
		[CommandError]
		public function getIncommingError(fault:*,event:GetIncommingEvent):void{
			this.incommingData=new ArrayCollection();
			stateIncomming = IN_DEFAULT;
			updatingIncoming=false;
		}
		
		[CommandResult]
		public function getIncommingByDayResult(result:*,event:GetIncommingByDayEvent):void{
			this.incommingData.removeAll();
			stateIncomming = IN_DAY;
			this.incommingData.addAll(translateDataByDay(result));
			updatingIncoming=false;
		}
		
		[CommandError]
		public function getIncommingByDayError(fault:*,event:GetIncommingByDayEvent):void{
			this.incommingData.removeAll();
			stateIncomming = IN_DAY;
			updatingIncoming=false;
		}
		
		
		[CommandResult]
		public function getIncommingSendersByDayResult(result:*,event:GetTopTenIncommingSendersByDayEvent):void{
			this.incommingSendersData=translateMail(result);
			updatingIncomingSenders=false;
		}
		
		[CommandError]
		public function getIncommingSendersByDayError(fault:*,event:GetTopTenIncommingSendersByDayEvent):void{
			this.incommingSendersData=new ArrayCollection();
			updatingIncomingSenders=false;
		}
		
		
		[CommandResult]
		public function getOutgoingResult(result:*,event:GetOutgoingEvent):void{
			this.outgoingData.removeAll();
			stateOutgoing = OUT_DEFAULT;
			this.outgoingData.addAll(translateData(result));
			updatingOutgoing=false;
		}
		
		[CommandError]
		public function getOutgoingError(fault:*,event:GetOutgoingEvent):void{
			this.outgoingData=new ArrayCollection();
			stateOutgoing = OUT_DEFAULT;
			updatingOutgoing=false;
		}
		
		[CommandResult]
		public function getOutgoingByDayResult(result:*,event:GetOutgoingByDayEvent):void{
			this.outgoingData.removeAll();
			stateOutgoing = OUT_DAY;
			this.outgoingData.addAll(translateDataByDay(result));
			updatingOutgoing=false;
		}
		
		[CommandError]
		public function getOutgoingByDayError(fault:*,event:GetOutgoingByDayEvent):void{
			this.outgoingData.removeAll();
			stateOutgoing = OUT_DAY;
			updatingOutgoing=false;
		}
		
		[CommandResult]
		public function getOutgoingRecipientsByDayResult(result:*,event:GetTopTenOutgoingRecipientsByDayEvent):void{
			this.outgoingRecipientsData=translateMail(result);
			updatingOutgoingRecipients=false;
		}
		
		[CommandError]
		public function getOutgoingRecipientsByDayError(fault:*,event:GetTopTenOutgoingRecipientsByDayEvent):void{
			this.outgoingRecipientsData=new ArrayCollection();
			updatingOutgoingRecipients=false;
		}
		
		private function translateData(result:*):ArrayCollection{
			var newData:ArrayCollection = new ArrayCollection();
			if(result!=null){
				if(result is Array || result is ArrayCollection){
					for each(var object:Object in result){
						var day:Date = object.date;
						day.setTime(day.getTime()+day.getTimezoneOffset()*60*1000);
						newData.addItem({Qty: object.count, MB:int((((object.bytes)/1024)/1024)*1000)/1000, Date:day});
					}
				}
			}
			return newData;
		}
		
		private function translateDataByDay(result:*):ArrayCollection{
			var newData:ArrayCollection = new ArrayCollection();
			if(result!=null){
				if(result is Array || result is ArrayCollection){
					for each(var object:Object in result){
						var day:Date = new Date((object.date as Date).getTime());
						day.setUTCHours(object.hours);
						day.setTime(day.getTime()-day.getTimezoneOffset()*60*1000);
						newData.addItem({Qty: object.count, MB:int((((object.bytes)/1024)/1024)*1000)/1000, Date:day});
					}
				}
			}
			return newData;
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