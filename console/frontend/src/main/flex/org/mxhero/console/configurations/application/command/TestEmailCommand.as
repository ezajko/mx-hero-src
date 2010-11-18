package org.mxhero.console.configurations.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.configurations.application.event.TestEmailEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;

	public class TestEmailCommand
	{
		[Inject(id="configurationService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:TestEmailEvent):AsyncToken
		{
			return service.testMail(event.config);
		}
		
		public function error(fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}