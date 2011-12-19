package org.mxhero.console.configurations.application.command
{
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.configurations.application.event.LoadAllGroupsEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.Group;
	import org.mxhero.console.frontend.domain.Page;

	public class LoadAllGroupsCommand
	{
		[Inject(id="groupService")]
		public var service:RemoteObject;
		
		[Inject]
		[Bindable]
		public var context:ApplicationContext;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:LoadAllGroupsEvent):AsyncToken
		{
			return service.findAll(event.domainId,event.pageNo,event.pageSize);
		}
		
	}
}