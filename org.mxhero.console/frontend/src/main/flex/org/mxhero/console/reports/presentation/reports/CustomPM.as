package org.mxhero.console.reports.presentation.reports
{
	import mx.collections.ArrayCollection;
	
	import org.mxhero.console.frontend.application.event.GetAccountsEvent;
	import org.mxhero.console.frontend.application.event.GetDomainAccountsEvent;
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
				dispatcher(new GetDomainAccountsEvent(context.selectedDomain.domain));
				dispatcher(new GetDomainGroupsEvent(context.selectedDomain.domain));
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