package org.mxhero.console.frontend.application.command
{
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	
	import mx.core.FlexGlobals;
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.frontend.application.event.LogoutEvent;

	public class LogoutCommand
	{
		[Inject(id="applicationUserService")]
		public var service:RemoteObject;
		
		public function execute(event:LogoutEvent):AsyncToken
		{
			return service.channelSet.logout()as AsyncToken;
		}
		
		public function result(result:*):void
		{	
			var urlRequest:URLRequest = new URLRequest(FlexGlobals.topLevelApplication.url);
			navigateToURL(urlRequest,"_self");
		}
		
		public function error(fault:*):void{
			var urlRequest:URLRequest = new URLRequest(FlexGlobals.topLevelApplication.url);
			navigateToURL(urlRequest,"_self");			
		}
	}
}