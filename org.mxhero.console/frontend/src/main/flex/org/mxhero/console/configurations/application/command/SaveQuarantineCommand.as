package org.mxhero.console.configurations.application.command
{
	
	import mx.rpc.AsyncToken;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.configurations.application.event.SaveQuarantineEvent;
	
	public class SaveQuarantineCommand
	{
		[Inject(id="quarantineService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:SaveQuarantineEvent):AsyncToken
		{
			return service.save(event.qurantine);
		}
	}
}