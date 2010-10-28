package org.mxhero.console.configurations.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.configurations.application.event.EditDomainEvent;

	public class EditDomainCommand
	{
		[Inject(id="domainService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:EditDomainEvent):AsyncToken
		{
			return service.edit(event.domain,event.hasOwner,event.password,event.email);
		}

	}
}