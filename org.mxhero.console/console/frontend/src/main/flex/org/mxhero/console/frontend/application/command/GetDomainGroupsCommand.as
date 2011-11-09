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
			return service.findAll(event.domainId);
		}
		
		public function result(result:*) : void {
			if (result is Group){
				context.groups=new ArrayCollection();
				context.groups.addItem(result);
			} else {
				context.groups=result;	
			}
			if(context.groups!=null){
				var sortByName:Sort=new Sort();
				sortByName.fields=[new SortField("name")];
				context.groups.sort=sortByName;
				context.groups.refresh();
			}
		}
	}
}