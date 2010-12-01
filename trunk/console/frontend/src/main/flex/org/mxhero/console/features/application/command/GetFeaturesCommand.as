package org.mxhero.console.features.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.features.application.event.GetFeaturesEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;

	public class GetFeaturesCommand
	{
		[Inject(id="featureService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:GetFeaturesEvent):AsyncToken
		{
			return service.findFeatures();
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}