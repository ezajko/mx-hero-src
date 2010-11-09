package org.mxhero.console.configurations.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.configurations.application.event.InsertGroupEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;

	public class InsertGroupCommand
	{
		[Inject(id="groupService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:InsertGroupEvent):AsyncToken
		{
			return service.insert(event.group,event.domainId,event.members);
		}

	}
}