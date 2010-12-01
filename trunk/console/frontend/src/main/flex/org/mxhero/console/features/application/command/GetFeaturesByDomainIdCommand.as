package org.mxhero.console.features.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.features.application.event.GetFeaturesByDomainIdEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;

	public class GetFeaturesByDomainIdCommand
	{
		[Inject(id="featureService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:GetFeaturesByDomainIdEvent):AsyncToken
		{
			return service.findFeaturesByDomainId(event.domainId);
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}