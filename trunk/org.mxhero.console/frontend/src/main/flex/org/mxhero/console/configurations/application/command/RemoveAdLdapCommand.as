package org.mxhero.console.configurations.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.configurations.application.event.RemoveAdLdapEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	
	public class RemoveAdLdapCommand
	{
		[Inject(id="domainService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:RemoveAdLdapEvent):AsyncToken
		{
			return service.removeAdLdap(event.domainId);
		}
		
	}
	
}