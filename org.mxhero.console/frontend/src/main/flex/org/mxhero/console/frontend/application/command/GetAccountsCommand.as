package org.mxhero.console.frontend.application.command
{
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.frontend.application.event.GetAccountsEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.EmailAccount;

	public class GetAccountsCommand
	{
		[Inject(id="emailAccountService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Inject]
		public var context:ApplicationContext;
		
		public function execute(event:GetAccountsEvent):AsyncToken
		{
			return service.findPageBySpecs(event.domainId,event.filterAccount,null,0,20);
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
		
		public function result (result:*):void{
			context.accounts=result.elements;
		}
	}
}