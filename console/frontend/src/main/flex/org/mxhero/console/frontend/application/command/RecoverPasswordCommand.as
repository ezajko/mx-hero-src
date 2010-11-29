package org.mxhero.console.frontend.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.frontend.application.event.RecoverPasswordEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;

	public class RecoverPasswordCommand
	{
		[Inject(id="applicationUserService")]
		[Bindable]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:RecoverPasswordEvent):AsyncToken
		{
			return service.sendPassword(event.mail);
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}