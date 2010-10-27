package org.mxhero.console.configurations.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.configurations.application.event.RemoveDomainEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;

	public class RemoveDomainCommand
	{
		[Inject(id="domainService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:RemoveDomainEvent):AsyncToken
		{
			return service.remove(event.id);
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}