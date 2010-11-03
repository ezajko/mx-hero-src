package org.mxhero.console.configurations.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.configurations.application.event.InsertEmailAccountEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;

	public class InsertEmailAccountCommand
	{
		[Inject(id="emailAccountService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:InsertEmailAccountEvent):AsyncToken
		{
			return service.insert(event.emailAccount,event.domainId);
		}

	}
}