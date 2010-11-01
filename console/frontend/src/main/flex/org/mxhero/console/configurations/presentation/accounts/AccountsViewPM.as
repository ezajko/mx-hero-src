package org.mxhero.console.configurations.presentation.accounts
{
	import flash.display.DisplayObject;
	
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	
	import org.mxhero.console.configurations.application.ConfigurationsDestinations;
	import org.mxhero.console.configurations.application.event.LoadAllEmailAccountsEvent;
	import org.mxhero.console.configurations.presentation.ConfigurationsViewPM;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.EmailAccount;
	import org.mxhero.console.frontend.domain.Page;

	[Landmark(name="main.dashboard.configurations.accounts")]
	public class AccountsViewPM
	{
		[Bindable]
		public var pageSize:Number=20;
		
		[Bindable]
		public var actualPage:Page;
		
		[Inject]
		[Bindable]
		public var context:ApplicationContext;
		
		[Inject]
		[Bindable]
		public var parentModel:ConfigurationsViewPM;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Bindable]
		public var isLoading:Boolean=false;
		
		[Bindable]
		public var selectEmailAccount:Object;
		
		private var _emailFilter:String=null;
		private var _nameFilter:String=null;
		private var _lastNameFilter:String=null;
		
		public function goBack():void{
			parentModel.navigateTo(ConfigurationsDestinations.LIST);
		}
		
		[Enter(time="every")]
		public function every():void{
			loadEmailAccounts();
		}
		
		public function loadEmailAccounts():void{
			findAccounts(context.selectedDomain.id,null,null,null,0,this.pageSize);
		}
		
		public function filterEmailAccounts(email:String,name:String,lastName:String):void{
			findAccounts(context.selectedDomain.id,email,name,lastName,actualPage.number+1,this.pageSize);
		}
		
		[CommandResult]
		public function loadingResult (result:*, event:LoadAllEmailAccountsEvent) : void {
			actualPage=result;
			isLoading=false;
		}
		
		[CommandError]
		public function loadingError (fault:*, event:LoadAllEmailAccountsEvent) : void {
			isLoading=false;
		}
		
		public function newEmailAccount(parent:DisplayObject):void{
			
		}

		public function removeEmailAccount():void{
			
		}

		public function editEmailAccount(parent:DisplayObject):void{
			
		}

		private function findAccounts(domainId:Number,email:String,name:String,lastName:String,page:Number,pageSize:Number):void{
			dispatcher(new LoadAllEmailAccountsEvent(domainId,email,name,lastName,page,pageSize));
			isLoading=true;
		}
	}
}