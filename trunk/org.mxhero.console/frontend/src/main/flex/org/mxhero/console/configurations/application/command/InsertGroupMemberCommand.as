package org.mxhero.console.configurations.application.command
{

	import mx.rpc.AsyncToken;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.configurations.application.event.InsertGroupMemberEvent;
	
	public class InsertGroupMemberCommand
	{
		[Inject(id="groupService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:InsertGroupMemberEvent):AsyncToken
		{
			return service.insertGroupMember(event.group,event.accounts);
		}
	}
}