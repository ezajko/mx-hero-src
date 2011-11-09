package org.mxhero.console.home.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.domain.FeatureRuleDirection;
	import org.mxhero.console.home.application.event.GetTopTenRecipientsTodayEvent;
	
	public class GetTopTenRecipientsTodayCommand
	{
		[Inject(id="toptenReportService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:GetTopTenRecipientsTodayEvent):AsyncToken
		{
			var sinceDay:Date = new Date();
			sinceDay.setHours(0,0,0,0);
			var untilDay:Date = new Date();
			var from:FeatureRuleDirection = new FeatureRuleDirection();
			from.directionType=FeatureRuleDirection.ANYONE;
			from.freeValue=FeatureRuleDirection.ANYONE;
			var to:FeatureRuleDirection = new FeatureRuleDirection();
			
			if(event.domainId==null){
				to.directionType=FeatureRuleDirection.ALLDOMAINS;
				to.freeValue=FeatureRuleDirection.ALLDOMAINS;
			}else{
				to.domain=event.domainId;
				to.freeValue=event.domainId;
				to.directionType=FeatureRuleDirection.DOMAIN;
			}
			
			return service.getTopTenRecipients(from,to,sinceDay.getTime(),untilDay.getTime());
			
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}