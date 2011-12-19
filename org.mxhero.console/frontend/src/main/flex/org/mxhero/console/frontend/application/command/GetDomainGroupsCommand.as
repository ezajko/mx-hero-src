package org.mxhero.console.frontend.application.command
{
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	import mx.rpc.AsyncToken;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.frontend.application.event.GetDomainGroupsEvent;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.Group;
	import org.mxhero.console.frontend.domain.Page;

	public class GetDomainGroupsCommand
	{
		[Inject(id="groupService")]
		public var service:RemoteObject;
		
		[Inject]
		[Bindable]
		public var context:ApplicationContext;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:GetDomainGroupsEvent):AsyncToken
		{
			return service.findAll(event.domainId,-1,-1);
		}
		
		public function result(result:*) : void {
			if(result!=null){
				context.groups=(result as Page).elements;
			}else{
				context.groups=null;
			}
		}
	}
}