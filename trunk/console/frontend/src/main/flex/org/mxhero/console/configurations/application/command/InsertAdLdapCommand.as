package org.mxhero.console.configurations.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.configurations.application.event.InsertAdLdapEvent;
	import org.mxhero.console.configurations.application.event.LoadAllDomainsEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	
	public class InsertAdLdapCommand
	{
		[Inject(id="domainService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:InsertAdLdapEvent):AsyncToken
		{
			return service.insertAdLdap(event.adLdap);
		}
		
	}

}