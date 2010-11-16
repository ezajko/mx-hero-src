package org.mxhero.console.configurations.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.configurations.application.event.EditApplicationUserEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.domain.ApplicationContext;

	public class EditApplicationUserCommand
	{
		[Inject(id="applicationUserService")]
		public var service:RemoteObject;
		
		[Inject]
		[Bindable]
		public var context:ApplicationContext;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:EditApplicationUserEvent):AsyncToken
		{
			return service.edit(event.user);
		}
		
		public function result(result:*):void{
			context.applicationUser=result;
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
		
	}
}