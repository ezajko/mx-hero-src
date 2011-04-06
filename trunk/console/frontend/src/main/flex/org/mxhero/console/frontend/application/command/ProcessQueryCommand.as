package org.mxhero.console.frontend.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.frontend.application.event.ProcessQueryEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;

	public class ProcessQueryCommand
	{
		[Inject(id="pluginReportService")]
		[Bindable]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:ProcessQueryEvent):AsyncToken
		{
			return service.getResult(event.query,event.params);
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}