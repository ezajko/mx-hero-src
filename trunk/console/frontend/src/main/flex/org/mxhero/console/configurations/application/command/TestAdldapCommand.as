package org.mxhero.console.configurations.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.configurations.application.event.TestAdldapEvent;

	public class TestAdldapCommand
	{
		[Inject(id="domainService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:TestAdldapEvent):AsyncToken
		{
			return service.testAdLdap(event.adLdap);
		}
		
	}
}