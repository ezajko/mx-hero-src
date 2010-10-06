package org.mxhero.console.frontend.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.frontend.application.event.LoginEvent;

	public class LoginCommand
	{
		[Inject(id="applicationUserService")]
		public var service:RemoteObject;
		
		public function execute(event:LoginEvent):AsyncToken
		{
			if(service.channelSet.authenticated){
				service.channelSet.disconnectAll();
			}
			return service.channelSet.login(event.username,event.password)as AsyncToken;
		}
		
		public function result(result:*, event:LoginEvent):void
		{
			trace(event);
		}

	}
}