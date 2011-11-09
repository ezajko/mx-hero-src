package org.mxhero.console.configurations.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.configurations.application.event.LoadEmailAccountsByGroupEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;

	public class LoadEmailAccountsByGroupCommand
	{
		[Inject(id="groupService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:LoadEmailAccountsByGroupEvent):AsyncToken
		{
			return service.findMembersByGroupId(event.groupName,event.domainId);
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}