package org.mxhero.console.features.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.features.application.event.GetRulesByDomainIdEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;

	public class GetRulesByDomainIdCommand
	{
		[Inject(id="featureService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:GetRulesByDomainIdEvent):AsyncToken
		{
			return service.GetRulesByDomainId(event.featureId,event.domainId);
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}