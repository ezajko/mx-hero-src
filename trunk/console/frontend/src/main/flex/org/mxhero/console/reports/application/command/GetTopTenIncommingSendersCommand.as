package org.mxhero.console.reports.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.domain.FeatureRuleDirection;
	import org.mxhero.console.reports.application.event.GetTopTenIncommingSendersEvent;

	public class GetTopTenIncommingSendersCommand
	{
		[Inject(id="customReportService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:GetTopTenIncommingSendersEvent):AsyncToken
		{
			var sinceDay:Date = new Date();
			sinceDay.setTime(event.since.getTime());
			sinceDay.setHours(0,0,0,0);
			var untilDay:Date = new Date();
			untilDay.setTime(untilDay.getTime()+24*60*60*1000);
			untilDay.setHours(0,0,0,0);
			var to:FeatureRuleDirection = new FeatureRuleDirection();
			to.directionType=FeatureRuleDirection.ANYONE;
			to.freeValue=FeatureRuleDirection.ANYONE;
			var from:FeatureRuleDirection = new FeatureRuleDirection();
			
			if(event.domainId==null){
				//if we get here we are at the admin view
				if(event.onlyDomain){
					//only senders that are allocated in one domain in the platform
					from.directionType=FeatureRuleDirection.ALLDOMAINS;
					from.freeValue=FeatureRuleDirection.ALLDOMAINS;
				}else{
					//any sender o recipients is the same thing
					from.directionType=FeatureRuleDirection.ANYONE;
					from.freeValue=FeatureRuleDirection.ANYONE;
				}
			}else{
				//if we get here we are in the domain view
				if(event.onlyDomain){
					//only senders from this domain
					from.domain=event.domainId;
					from.freeValue=event.domainId;
					from.directionType=FeatureRuleDirection.DOMAIN;
				}else{
					//senders can be from any domain but recipients must be in the domain
					from.directionType=FeatureRuleDirection.ANYONE;
					from.freeValue=FeatureRuleDirection.ANYONE;
					to.domain=event.domainId;
					to.freeValue=event.domainId;
					to.directionType=FeatureRuleDirection.DOMAIN;
				}
			}
			
			return service.getTopTenSenders(from,to,sinceDay.getTime(),untilDay.getTime());
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}