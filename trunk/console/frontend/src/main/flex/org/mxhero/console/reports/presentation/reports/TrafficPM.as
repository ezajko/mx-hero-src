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
	import org.mxhero.console.reports.application.event.GetTopTenIncommingSendersEvent;
	import org.mxhero.console.reports.application.event.GetTopTenOutgoingRecipientsByDayEvent;
	import org.mxhero.console.reports.application.event.GetTopTenOutgoingRecipientsEvent;
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
		
		private var since24Hs:Date = new Date();
		
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
			since24Hs.setTime(new Date());
			since24Hs.setTime(since24Hs.getTime()-PLUSDAY);
			getIncomming();
			getOutgoing();
		}
		
		public function getIncomming():void{
			
			if(context.selectedDomain!=null){
				dispatcher(new GetIncommingEvent(context.selectedDomain.domain,sinceDate));
				dispatcher(new GetTopTenIncommingSendersEvent(context.selectedDomain.domain,since24Hs,this.onlyDomain));
			}else{
				dispatcher(new GetIncommingEvent(null,sinceDate));
				dispatcher(new GetTopTenIncommingSendersEvent(null,since24Hs,this.onlyDomain));
			}
			updatingIncoming=true;
			updatingIncomingSenders=true;
		}
		
		public function getIncommingByDay(day:Date):void{
			var sinceDay:Date = new Date();
			sinceDay.setTime(day.getTime());
			sinceDay.setHours(0,0,0,0);
			
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
			if(context.selectedDomain!=null){
				dispatcher(new GetOutgoingEvent(context.selectedDomain.domain,sinceDate));
				dispatcher(new GetTopTenOutgoingRecipientsEvent(context.selectedDomain.domain,sinceDate,this.onlyDomain));
			}else{
				dispatcher(new GetOutgoingEvent(null,sinceDate));
				dispatcher(new GetTopTenOutgoingRecipientsEvent(null,sinceDate,this.onlyDomain));
			}
			updatingOutgoing=true;
			updatingOutgoingRecipients=true;
		}
		
		public function getOutgoingByDay(day:Date):void{
			var sinceDay:Date = new Date();
			sinceDay.setTime(day.getTime());
			sinceDay.setHours(0,0,0,0);
			
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
		public function getIncommingSendersResult(result:*,event:GetTopTenIncommingSendersEvent):void{
			this.incommingSendersData=translateMail(result);
			updatingIncomingSenders=false;
		}
		
		[CommandError]
		public function getIncommingSendersError(fault:*,event:GetTopTenIncommingSendersEvent):void{
			this.incommingSendersData=new ArrayCollection();
			updatingIncomingSenders=false;
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
		public function getOutgoingRecipientsResult(result:*,event:GetTopTenOutgoingRecipientsEvent):void{
			this.outgoingRecipientsData=translateMail(result);
			updatingOutgoingRecipients=false;
		}
		
		[CommandError]
		public function getOutgoingRecipientsError(fault:*,event:GetTopTenOutgoingRecipientsEvent):void{
			this.outgoingRecipientsData=new ArrayCollection();
			updatingOutgoingRecipients=false;
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
						var day:Date = object[2];
						day.setTime(day.getTime()+day.getTimezoneOffset()*60*1000);
						newData.addItem({Qty: object[0], MB:int((((object[1])/1024)/1024)*1000)/1000, Date:day});
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
						var day:Date = new Date((object[2]as Date).getTime());
						day.setUTCHours(object[3]);
						day.setTime(day.getTime()-day.getTimezoneOffset()*60*1000);
						newData.addItem({Qty: object[0], MB:int((((object[1])/1024)/1024)*1000)/1000, Date:day});
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
						newData.addItem({Qty: object[0], Email:object[1]});
					}
				}
				newData.source = newData.source.reverse();
			}
			return newData;
		}
		
	}
}