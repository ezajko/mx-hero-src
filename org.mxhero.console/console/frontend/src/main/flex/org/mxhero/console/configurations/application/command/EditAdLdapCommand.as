package org.mxhero.console.configurations.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.configurations.application.event.EditAdLdapEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	
	public class EditAdLdapCommand
	{
		[Inject(id="domainService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:EditAdLdapEvent):AsyncToken
		{
			return service.editAdLdap(event.adLdap);
		}
		
	}

}