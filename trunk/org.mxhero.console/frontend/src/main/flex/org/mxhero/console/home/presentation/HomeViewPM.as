package org.mxhero.console.home.presentation
{
	import com.adobe.cairngorm.navigation.NavigationEvent;
	
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	
	import mx.charts.DateTimeAxis;
	import mx.collections.ArrayCollection;
	import mx.controls.DateField;
	import mx.formatters.DateFormatter;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	
	import org.mxhero.console.frontend.application.event.GetDomainsEvent;
	import org.mxhero.console.frontend.domain.ActivityData;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.Domain;
	import org.mxhero.console.frontend.domain.MessagesComposition;
	import org.mxhero.console.frontend.domain.MxHeroData;
	import org.mxhero.console.home.application.event.GetActivityByHoursEvent;
	import org.mxhero.console.home.application.event.GetActivityEvent;
	import org.mxhero.console.home.application.event.GetMessagesCompositionEvent;
	import org.mxhero.console.home.application.event.GetMxHeroDataEvent;
	import org.mxhero.console.home.application.event.GetTopTenRecipientsTodayEvent;
	import org.mxhero.console.home.application.event.GetTopTenSendersTodayEvent;
	import org.mxhero.console.home.application.resources.HomeProperties;
	
	[Landmark(name="main.dashboard.home")]
	public class HomeViewPM
	{
		[MessageDispatcher]
		public var dispatcher:Function;
		public static var refreshFeed:Function;
		private static const DOMAIN_STATE:String = "domain";
		private static const ADMIN_STATE:String = "admin";
		private static const FILTER_STATE:String = "filter";
		public static const PERIOD_24HOURS:String = "hours24";
		public static const PERIOD_HOUR:String = "hour";
		
		[Bindable]
		public var rs:IResourceManager = ResourceManager.getInstance();
		[Inject]
		[Bindable]
		public var context:ApplicationContext;
		[Bindable]
		public var currentState:String = DOMAIN_STATE;
		[Bindable]
		public var sendersData:ArrayCollection=null;
		[Bindable]
		public var recipientsData:ArrayCollection=null;
		[Bindable]
		public var messageComposition:ArrayCollection=null;
		[Bindable]
		public var domainFilter:String;
		[Bindable]
		public var mxHeroData:MxHeroData;
		[Bindable]
		public var hourSince:Date;
		[Bindable]
		public var virusActivity:ArrayCollection=null;
		[Bindable]
		public var spamActivity:ArrayCollection=null;
		[Bindable]
		public var incommingActivity:ArrayCollection=null;
		[Bindable]
		public var outgoingActivity:ArrayCollection=null;
		[Bindable]
		public var blockActivity:ArrayCollection=null;
		[Bindable]
		public var totals:ArrayCollection=null;
		private var timer:Timer = new Timer(15000,1);
		private var completeTimer:Timer = new Timer(60000,1);
		[Bindable]
		public var centralState:String = 'default';
		
		private var hasToRefresh:Boolean = false;
		[Bindable]
		public var periodIndex:Number=1;
		
		public function HomeViewPM(){
			timer.addEventListener(TimerEvent.TIMER,refreshActivity);
			completeTimer.addEventListener(TimerEvent.TIMER,getCompleteData);
		}
		
		[Enter(time="every")]
		public function enter():void
		{
			periodIndex=1;
			hasToRefresh=true;
			if(context.selectedDomain!=null){
				currentState=DOMAIN_STATE;
				domainFilter=context.selectedDomain.domain;
			}else{
				currentState=ADMIN_STATE;
				if(context.selectedDomain!=null){
					domainFilter=context.selectedDomain.domain;
				}else{
					domainFilter=null;
				}
				dispatcher(new GetDomainsEvent());
			}
			getCompleteData();
			refreshActivity();
			if(refreshFeed!=null){
				refreshFeed();
			}
			if(context.configuration!=null
			&& context.configuration.newsFeedEnabled!=null
			&& context.configuration.newsFeedEnabled=="true"){
				centralState='default'
			}else{
				centralState='nonews';
			}
		}
		
		[Exit]
		public function exit():void{
			hasToRefresh=false;
			timer.stop();
			timer.reset();
			completeTimer.stop();
			completeTimer.reset();
		}
		
		public function filterDomain(filterDomain:String):void{
			dispatcher(new GetDomainsEvent(filterDomain));
		}
		
		public function periodChanged(period:String):void{
			if(period==PERIOD_24HOURS){
				timer.stop();
				timer.reset();
				this.spamActivity=null;
				this.virusActivity=null;
				this.incommingActivity=null;
				this.outgoingActivity=null;
				this.blockActivity=null;
				this.totals=null;
				this.hourSince=new Date();
				this.hourSince.time = this.hourSince.time - 24*60*60*1000;
				this.hourSince.time = this.hourSince.setMinutes(0,0,0);
				dispatcher(new GetActivityByHoursEvent(hourSince,domainFilter));
				//24HOURSPERIOD
			}else if(period==PERIOD_HOUR){
				refreshActivity();
			}
		}
		
		public function getCompleteData(event:*=null):void{		
			if(context.selectedDomain!=null){
				currentState=DOMAIN_STATE;
			}else{
				if(domainFilter==null){
					currentState=ADMIN_STATE;
				}else{
					currentState=FILTER_STATE;
				}
			}
			var since:Date = new Date();
			since.setHours(0,0,0,0);
			dispatcher(new GetTopTenSendersTodayEvent(domainFilter));
			dispatcher(new GetTopTenRecipientsTodayEvent(domainFilter));
			dispatcher(new GetMxHeroDataEvent(domainFilter));
			dispatcher(new GetMessagesCompositionEvent(since,domainFilter));
		}
		
		private function startTimer():void{
			timer.stop();
			timer.reset();
			if(hasToRefresh==true){
				timer.start();
			}
		}
		
		private function startCompleteTimer():void{
			completeTimer.stop();
			completeTimer.reset();
			if(hasToRefresh==true){
				completeTimer.start();
			}
		}
		
		public function refreshActivity(event:*=null):void{
			this.hourSince=new Date();
			this.hourSince.time = this.hourSince.time - 1*60*60*1000;
			this.hourSince.time = this.hourSince.setSeconds(0,0);
			dispatcher(new GetActivityEvent(hourSince,domainFilter));
		}
		
		[CommandResult]
		public function getActivityEventResult(result:*,event:GetActivityEvent):void{
			this.spamActivity=translateActivity((result as ActivityData).spam);
			this.virusActivity=translateActivity((result as ActivityData).virus);
			this.incommingActivity=translateActivity((result as ActivityData).incomming);
			this.outgoingActivity=translateActivity((result as ActivityData).outgoing);
			this.blockActivity=translateActivity((result as ActivityData).blocked);
			this.totals=getTotals();
			startTimer();
		}
		
		[CommandResult]
		public function getActivityByHoursResult(result:*,event:GetActivityByHoursEvent):void{
			this.spamActivity=translateActivityByHours((result as ActivityData).spam);
			this.virusActivity=translateActivityByHours((result as ActivityData).virus);
			this.incommingActivity=translateActivityByHours((result as ActivityData).incomming);
			this.outgoingActivity=translateActivityByHours((result as ActivityData).outgoing);
			this.blockActivity=translateActivityByHours((result as ActivityData).blocked);
			this.totals=getTotals();
			startTimer();
		}
		
		[CommandError]
		public function getActivityEventError(fault:*,event:GetActivityEvent):void{
			this.spamActivity=null;
			this.virusActivity=null;
			this.incommingActivity=null;
			this.outgoingActivity=null;
			this.blockActivity=null;
			this.totals=null;
			startTimer();
		}
		
		[CommandResult]
		public function getMessagesCompositionEventResult(result:*,event:GetMessagesCompositionEvent):void{
			var composition:MessagesComposition = result as MessagesComposition;
			this.messageComposition=new ArrayCollection([{Stat:(rs.getString(HomeProperties.NAME,HomeProperties.RIGTH_MESSAGES_SPAM)),Qty:composition.spam}
			,{Stat:(rs.getString(HomeProperties.NAME,HomeProperties.RIGTH_MESSAGES_VIRUS)),Qty:composition.virus}
			,{Stat:(rs.getString(HomeProperties.NAME,HomeProperties.RIGTH_MESSAGES_CLEAN)),Qty:composition.clean}
			,{Stat:(rs.getString(HomeProperties.NAME,HomeProperties.RIGTH_MESSAGES_BLOCKED)),Qty:composition.blocked}]);
			startCompleteTimer();
		}
		
		[CommandError]
		public function getMessagesCompositionEventError(fault:*,event:GetMessagesCompositionEvent):void{
			this.mxHeroData=null;
		}
		
		[CommandResult]
		public function getMxHeroDataEventResult(result:*,event:GetMxHeroDataEvent):void{
			this.mxHeroData=result;
		}
		
		[CommandError]
		public function getMxHeroDataEventError(fault:*,event:GetMxHeroDataEvent):void{
			this.mxHeroData=null;
		}
		
		[CommandResult]
		public function getTopTenSendersTodayEventResult(result:*,event:GetTopTenSendersTodayEvent):void{
			this.sendersData=translateMail(result);
		}
		
		[CommandError]
		public function getTopTenSendersTodayEventError(fault:*,event:GetTopTenSendersTodayEvent):void{
			this.sendersData=null;
		}
		
		[CommandResult]
		public function getTopTenRecipientsTodayEventResult(result:*,event:GetTopTenRecipientsTodayEvent):void{
			this.recipientsData=translateMail(result);
		}
		
		[CommandError]
		public function getTopTenRecipientsTodayEventError(fault:*,event:GetTopTenRecipientsTodayEvent):void{
			this.recipientsData=null;
		}
		
		public function navigateTo(destination:String):void
		{
			dispatcher(NavigationEvent.createNavigateToEvent(destination));
		}
		
		private function translateMail(result:*):ArrayCollection{
			var newData:ArrayCollection = new ArrayCollection();
			if(result!=null){
				if(result is Array || result is ArrayCollection){
					for each(var object:Object in result){
						newData.addItem({Qty: object.count, Label:object.label});
					}
				}
				newData.source = newData.source;
			}
			return newData;
		}
		
		private function translateActivity(data:ArrayCollection):ArrayCollection{
			var activityArray:ArrayCollection = new ArrayCollection();
			for (var i:int=0;i<61;i++){
				var newDate:Date= new Date();
				newDate.time=hourSince.time;
				newDate.time=newDate.time+i*60*1000;
				activityArray.addItem({Qty:0,Date:newDate});
			}
			for each(var item:Object in data){
				var dateString:String = (item.date as String).split(" ")[0];
				var hourString:String = (item.date as String).split(" ")[1];
				var date:Date = DateField.stringToDate(dateString,"YYYY-MM-DD");
				date.time=Date.UTC(date.fullYear,date.month,date.date,new Number(hourString.split(":")[0]),new Number(hourString.split(":")[1]));
				var index:int = (date.time-hourSince.time)/(60*1000);
				if(index<activityArray.length){
					activityArray.setItemAt({Qty:item.count,Date:date},index);
				}else{
					activityArray.addItem({Qty:item.count,Date:date});
				}
			}
			return activityArray;
		}
		
		private function translateActivityByHours(data:ArrayCollection):ArrayCollection{
			var activityArray:ArrayCollection = new ArrayCollection();
			for (var i:int=0;i<25;i++){
				var newDate:Date= new Date();
				newDate.time=hourSince.time;
				newDate.time=newDate.time+i*60*60*1000;
				activityArray.addItem({Qty:0,Date:newDate});
			}
			for each(var item:Object in data){
				var date:Date = item.date as Date;
				date.time=Date.UTC(date.fullYear,date.month,date.date,item.hours);
				var index:int = (date.time-hourSince.time)/(60*60*1000);
				if(index<activityArray.length){
					activityArray.setItemAt({Qty:item.count,Date:date},index);
				}else{
					activityArray.addItem({Qty:item.count,Date:date});
				}
			}
			return activityArray;
		}
		
		private function getTotal(data:ArrayCollection):Number{
			var total:Number = 0;
			for each(var item:Object in data){
				total = total +new Number(item.Qty);
			}
			return total;
		}
		
		private function getTotals():ArrayCollection{
			var totals:ArrayCollection = new ArrayCollection();
			totals.addItem({Qty:getTotal(incommingActivity),Label:(rs.getString(HomeProperties.NAME,HomeProperties.RIGTH_ACTIVITY_INCOMMING))});
			totals.addItem({Qty:getTotal(outgoingActivity),Label:(rs.getString(HomeProperties.NAME,HomeProperties.RIGTH_ACTIVITY_OUTGOING))});
			totals.addItem({Qty:getTotal(spamActivity),Label:(rs.getString(HomeProperties.NAME,HomeProperties.RIGTH_ACTIVITY_SPAM))});
			totals.addItem({Qty:getTotal(blockActivity),Label:(rs.getString(HomeProperties.NAME,HomeProperties.RIGTH_ACTIVITY_BLOCKED))});
			totals.addItem({Qty:getTotal(virusActivity),Label:(rs.getString(HomeProperties.NAME,HomeProperties.RIGTH_ACTIVITY_VIRUS))});
			return totals;
		}
		
	}
}