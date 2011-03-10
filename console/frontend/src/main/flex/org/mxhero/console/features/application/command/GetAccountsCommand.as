package org.mxhero.console.features.application.command
{
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.features.application.event.GetAccountsEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.EmailAccount;
	import org.mxhero.console.frontend.domain.Page;

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
			return service.findAll();
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
		
		public function result (result:*):void{

			if(result is EmailAccount){
				context.accounts=new ArrayCollection();
				context.accounts.addItem(result);
			}else {
				context.accounts=result;
			}		

			if(context.accounts!=null){
				//var sortByName:Sort=new Sort();
				//sortByName.fields=[new SortField("domain"), new SortField("account")];
				//context.accounts.sort=sortByName;
				//context.accounts.refresh();
			}
		}
	}
}