package org.mxhero.console.frontend.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.frontend.application.event.LoginEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;

	public class LoginCommand
	{
		[Inject(id="applicationUserService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:LoginEvent):AsyncToken
		{
			if(service.channelSet.authenticated){
				service.channelSet.disconnectAll();
			}
			return service.channelSet.login(event.username,event.password)as AsyncToken;
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}

	}
}