package org.mxhero.console.configurations.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.configurations.application.event.UploadAccountsEvent;

	public class UploadAccountsCommand
	{
		[Inject(id="emailAccountService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:UploadAccountsEvent):AsyncToken
		{
			return service.upload(event.accounts,event.domainId,event.failOnError);
		}
		
	}
}