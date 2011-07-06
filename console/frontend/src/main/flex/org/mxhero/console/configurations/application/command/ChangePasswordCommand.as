package org.mxhero.console.configurations.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.configurations.application.event.ChangePasswordEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.infrastructure.AuthorizeHelper;

	public class ChangePasswordCommand
	{
		[Inject(id="applicationUserService")]
		public var service:RemoteObject;
		[Inject]
		public var helper:AuthorizeHelper;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:ChangePasswordEvent):AsyncToken
		{
			if(helper.checkUserForAuthority("ROLE_ADMIN") && event.domainOwner>-1){
				return service.changePassword(event.oldPassword,event.newPassword,event.domainOwner);
			}
			return service.changePassword(event.oldPassword,event.newPassword);
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}