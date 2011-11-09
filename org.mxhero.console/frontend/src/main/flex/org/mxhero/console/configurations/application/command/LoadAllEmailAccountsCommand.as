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
			return service.findPageBySpecs(event.domainId,event.email,event.groupId);
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
				//var sortByName:Sort=new Sort();
				//sortByName.fields=[new SortField("domain"), new SortField("account")];
				//context.accounts.sort=sortByName;
				//context.accounts.refresh();
			}	
		}
	}
}