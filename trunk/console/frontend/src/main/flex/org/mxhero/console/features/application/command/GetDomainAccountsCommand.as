package org.mxhero.console.features.application.command
{
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.features.application.event.GetDomainAccountsEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.EmailAccount;

	public class GetDomainAccountsCommand
	{
		[Inject(id="emailAccountService")]
		public var service:RemoteObject;
		
		[Inject]
		public var context:ApplicationContext;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:GetDomainAccountsEvent):AsyncToken
		{
			return service.findByDomain(event.domainId);
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
		
		public function result (result:*):void{
			if(result is EmailAccount){
				var accountsArray:ArrayCollection = new ArrayCollection();
				accountsArray.addItem(result);
				context.accounts=accountsArray;
			} else {
				context.accounts=result;
			}
			if(context.accounts!=null){
				var sortByName:Sort=new Sort();
				sortByName.fields=[new SortField("domain"), new SortField("account")];
				context.accounts.sort=sortByName;
				context.accounts.refresh();
			}		
		}

	}
}