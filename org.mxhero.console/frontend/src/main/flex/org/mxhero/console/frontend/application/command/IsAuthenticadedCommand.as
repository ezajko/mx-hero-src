package org.mxhero.console.frontend.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.frontend.application.event.IsAuthenticadedEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.domain.ApplicationContext;

	public class IsAuthenticadedCommand
	{
		[Inject(id="applicationUserService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:IsAuthenticadedEvent):AsyncToken
		{
			return service.isAuthenticated();
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}