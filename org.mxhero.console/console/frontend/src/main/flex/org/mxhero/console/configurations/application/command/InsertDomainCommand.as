package org.mxhero.console.configurations.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.configurations.application.event.InsertDomainEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;

	public class InsertDomainCommand
	{
		[Inject(id="domainService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:InsertDomainEvent):AsyncToken
		{
			return service.insert(event.domain,event.hasOwner,event.password,event.email);
		}

	}
}