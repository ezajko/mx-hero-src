package org.mxhero.console.features.application.command
{
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.features.application.event.GetDomainsEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.Domain;

	public class GetDomainsCommand
	{
		[Inject(id="domainService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Inject]
		public var context:ApplicationContext;
		
		public function execute(event:GetDomainsEvent):AsyncToken
		{
			return service.findAll();
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
		
		public function result (result:*):void {
			if(result is Domain){
				context.domains=new ArrayCollection();
				context.domains.addItem(result);
			}else {
				context.domains=result;
			}
			if(context.domains!=null){
				//var sortByName:Sort=new Sort();
				//sortByName.fields=[new SortField("domain")];
				//context.domains.sort=sortByName;
				//context.domains.refresh();
			}
		}
	}
}