package org.mxhero.console.configurations.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.configurations.application.event.ChangePasswordEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;

	public class ChangePasswordCommand
	{
		[Inject(id="applicationUserService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:ChangePasswordEvent):AsyncToken
		{
			return service.changePassword(event.oldPassword,event.newPassword);
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}