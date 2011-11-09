package org.mxhero.console.configurations.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.configurations.application.event.LoadEmailAccountsWithNoGroupEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;

	public class LoadEmailAccountsWithNoGroupCommand
	{
		[Inject(id="groupService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:LoadEmailAccountsWithNoGroupEvent):AsyncToken
		{
			return service.findMembersByDomainIdWithoutGroup(event.domainId);
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}