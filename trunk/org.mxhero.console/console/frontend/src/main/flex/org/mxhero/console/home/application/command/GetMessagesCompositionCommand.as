package org.mxhero.console.home.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.home.application.event.GetMessagesCompositionEvent;
	
	public class GetMessagesCompositionCommand
	{
		[Inject(id="homeReportService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:GetMessagesCompositionEvent):AsyncToken
		{
			return service.getMessagesCompositionData(event.since.getTime(),event.domainId);
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}