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
		public var stateIncomming:String = IN_DEFAULT;
		
		[Bindable]
		public var stateOutgoing:String = OUT_DEFAULT;
		
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
			getIncomming();
			getOutgoing();
		}
		
		public function getIncomming():void{
			if(context.selectedDomain!=null){
				dispatcher(new GetIncommingEvent(context.selectedDomain.domain));
				dispatcher(new GetTopTenIncommingSendersEvent(context.selectedDomain.domain));
			}else{
				dispatcher(new GetIncommingEvent());
				dispatcher(new GetTopTenIncommingSendersEvent());
			}
			updatingIncoming=true;
			updatingIncomingSenders=true;
			stateIncomming = IN_DEFAULT;
			
		}
		
		public function getIncommingByDay(day:Date):void{
			if(context.selectedDomain!=null){
				dispatcher(new GetIncommingByDayEvent(context.selectedDomain.domain,day));
				dispatcher(new GetTopTenIncommingSendersByDayEvent(context.selectedDomain.domain,day));
			}else{
				dispatcher(new GetIncommingByDayEvent(null,day));
				dispatcher(new GetTopTenIncommingSendersByDayEvent(null,day));
			}
			updatingIncoming=true;
			updatingIncomingSenders=true;
			stateIncomming = IN_DAY;
		}
		
		public function getOutgoing():void{
			if(context.selectedDomain!=null){
				dispatcher(new GetOutgoingEvent(context.selectedDomain.domain));
				dispatcher(new GetTopTenOutgoingRecipientsEvent(context.selectedDomain.domain));
			}else{
				dispatcher(new GetOutgoingEvent());
				dispatcher(new GetTopTenOutgoingRecipientsEvent());
			}
			updatingOutgoing=true;
			updatingOutgoingRecipients=true;
			stateOutgoing = OUT_DEFAULT;
		}
		
		public function getOutgoingByDay(day:Date):void{
			if(context.selectedDomain!=null){
				dispatcher(new GetOutgoingByDayEvent(context.selectedDomain.domain,day));
				dispatcher(new GetTopTenOutgoingRecipientsByDayEvent(context.selectedDomain.domain,day));
			}else{
				dispatcher(new GetOutgoingByDayEvent(null,day));
				dispatcher(new GetTopTenOutgoingRecipientsByDayEvent(null,day));
			}
			updatingOutgoing=true;
			updatingOutgoingRecipients=true;
			stateOutgoing = OUT_DAY;
		}
		
		[CommandResult]
		public function getIncommingResult(result:*,event:GetIncommingEvent):void{
			this.incommingData=translateData(result);
			updatingIncoming=false;
		}
		
		[CommandError]
		public function getIncommingError(fault:*,event:GetIncommingEvent):void{
			this.incommingData=new ArrayCollection();
			updatingIncoming=false;
		}
		
		[CommandResult]
		public function getIncommingByDayResult(result:*,event:GetIncommingByDayEvent):void{
			this.incommingData=translateDataByDay(result);
			updatingIncoming=false;
		}
		
		[CommandError]
		public function getIncommingByDayError(fault:*,event:GetIncommingByDayEvent):void{
			this.incommingData=new ArrayCollection();
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
			this.outgoingData=translateData(result);
			updatingOutgoing=false;
		}
		
		[CommandError]
		public function getOutgoingError(fault:*,event:GetOutgoingEvent):void{
			this.outgoingData=new ArrayCollection();
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
			this.outgoingData=translateDataByDay(result);
			updatingOutgoing=false;
		}
		
		[CommandError]
		public function getOutgoingByDayError(fault:*,event:GetOutgoingByDayEvent):void{
			this.outgoingData=new ArrayCollection();
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
						newData.addItem({Qty: object[0], MB:int((((object[1])/1024)/1024)*1000)/1000, Date:object[2]});
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
						var day:Date = object[2];
						day.hoursUTC=object[3];
						day.minutes=0;
						day.seconds=0;
						day.milliseconds=0;
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