package org.mxhero.console.home.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.domain.FeatureRuleDirection;
	import org.mxhero.console.home.application.event.GetTopTenSendersTodayEvent;
	
	public class GetTopTenSendersTodayCommand
	{
		[Inject(id="toptenReportService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:GetTopTenSendersTodayEvent):AsyncToken
		{
			var sinceDay:Date = new Date();
			sinceDay.setHours(0,0,0,0);
			var untilDay:Date = new Date();
			var to:FeatureRuleDirection = new FeatureRuleDirection();
			to.directionType=FeatureRuleDirection.ANYONE;
			to.freeValue=FeatureRuleDirection.ANYONE;
			var from:FeatureRuleDirection = new FeatureRuleDirection();
			
			if(event.domainId==null){
				from.directionType=FeatureRuleDirection.ALLDOMAINS;
				from.freeValue=FeatureRuleDirection.ALLDOMAINS;
			}else{
				from.domain=event.domainId;
				from.freeValue=event.domainId;
				from.directionType=FeatureRuleDirection.DOMAIN;
			}
			
			return service.getTopTenSenders(from,to,sinceDay.getTime(),untilDay.getTime());
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}