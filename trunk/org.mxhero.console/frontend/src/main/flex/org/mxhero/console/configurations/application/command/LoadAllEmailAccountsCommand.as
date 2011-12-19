package org.mxhero.console.configurations.application.command
{
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.configurations.application.event.LoadAllEmailAccountsEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.EmailAccount;

	public class LoadAllEmailAccountsCommand
	{
		[Inject(id="emailAccountService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Inject]
		public var context:ApplicationContext;
		
		public function execute(event:LoadAllEmailAccountsEvent):AsyncToken
		{
			return service.findPageBySpecs(event.domainId,event.email,event.groupId,event.pageNo,event.pageSize);
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
		
		public function result (result:*):void{
			context.accounts=result;
		}
	}
}