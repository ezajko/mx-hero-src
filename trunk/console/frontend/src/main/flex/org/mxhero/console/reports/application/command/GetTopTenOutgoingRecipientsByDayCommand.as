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