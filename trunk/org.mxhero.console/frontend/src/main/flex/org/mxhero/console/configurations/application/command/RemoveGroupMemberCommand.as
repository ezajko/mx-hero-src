package org.mxhero.console.configurations.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.configurations.application.event.RemoveGroupMemberEvent;

	public class RemoveGroupMemberCommand
	{
		[Inject(id="groupService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:RemoveGroupMemberEvent):AsyncToken
		{
			return service.removeGroupMember(event.accounts);
		}
	}
}