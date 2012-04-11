package org.mxhero.console.configurations.application.command
{

	import mx.rpc.AsyncToken;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.configurations.application.event.LoadQuarantineEvent;
	
	public class LoadQuarantineCommand
	{
		[Inject(id="quarantineService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:LoadQuarantineEvent):AsyncToken
		{
			return service.read(event.domain);
		}
	}
}