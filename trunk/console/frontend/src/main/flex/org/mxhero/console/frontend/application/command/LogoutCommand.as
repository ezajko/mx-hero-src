package org.mxhero.console.frontend.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.frontend.application.event.LogoutEvent;

	public class LogoutCommand
	{
		[Inject(id="applicationUserService")]
		private var service:RemoteObject;
		
		public function execute(event:LogoutEvent):AsyncToken
		{
			return service.channelSet.logout()as AsyncToken;
		}
		
		public function result(event:LogoutEvent):void
		{
			
		}
	}
}