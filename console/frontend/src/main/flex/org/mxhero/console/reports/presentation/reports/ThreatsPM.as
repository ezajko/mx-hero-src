package org.mxhero.console.reports.presentation.reports
{
	import mx.collections.ArrayCollection;
	
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
		
		[Bindable]
		public var virusUpdating:Boolean=false;
		[Bindable]
		public var spamUpdating:Boolean=false;
		[Bindable]
		public var sinceDate:Date;
		
		public function goBack():void{
			parentModel.navigateTo(ReportsDestinations.LIST);
		}
		
		[Enter(time="every")]
		public function every():void{
			spamHists=null;
			spamMails=null;
			virusHists=null;
			virusMails=null;
			sinceDate = new Date();
			sinceDate.setTime(sinceDate.setUTCHours(0,0,0,0));
			sinceDate.setTime(sinceDate.getTime()-DAYSBEFORE);
			getVirus();
			getSpam();
		}
		
		public function getVirus():void{
			var domain:String = null;
			
			if(context.selectedDomain!=null){
				domain = context.selectedDomain.domain;
			}
			dispatcher( new GetVirusHitsEvent(domain,sinceDate) );
			dispatcher( new GetVirusEmailsEvent(domain,sinceDate,new Date()));
			stateVirus = "zoom_out";
			this.virusUpdating=true;
		}
		
		public function getSpam():void{
			var domain:String = null;
			
			if(context.selectedDomain!=null){
				domain = context.selectedDomain.domain;
			}
			dispatcher( new GetSpamHitsEvent(domain,sinceDate) );
			dispatcher( new GetSpamEmailsEvent(domain,sinceDate,new Date()));
			stateSpam = "zoom_out";
			this.spamUpdating=true;
		}
		
		public function getVirusByDay(day:Date):void{
			var domain:String = null;
			
			if(context.selectedDomain!=null){
				domain = context.selectedDomain.domain;
			}
			dispatcher( new GetVirusHitsDayEvent(domain,day) );
			dispatcher( new GetVirusEmailsEvent(domain,day,day));
			this.virusUpdating=true;
		}
		
		public function getSpamByDay(day:Date):void{
			var domain:String = null;
			
			if(context.selectedDomain!=null){
				domain = context.selectedDomain.domain;
			}
			dispatcher( new GetSpamHitsDayEvent(domain,day) );
			dispatcher( new GetSpamEmailsEvent(domain,day,day));
			this.spamUpdating=true;
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
						var day:Date = object[1];
						day.hoursUTC=object[2];
						day.minutes=0;
						day.seconds=0;
						day.milliseconds=0;
						newData.addItem({Qty: object[0], Date:day});
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
						newData.addItem({Qty: object[0], Date:object[1]});
					}
				}
			}
			return newData;
		}
	}
}