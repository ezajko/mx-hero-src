package org.mxhero.console.configurations.presentation.accounts
{
	import flash.display.DisplayObject;
	
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	import mx.rpc.events.FaultEvent;
	
	import org.mxhero.console.commons.infrastructure.ErrorTranslator;
	import org.mxhero.console.configurations.application.ConfigurationsDestinations;
	import org.mxhero.console.configurations.application.event.EditEmailAccountEvent;
	import org.mxhero.console.configurations.application.event.InsertEmailAccountEvent;
	import org.mxhero.console.configurations.application.event.LoadAllEmailAccountsEvent;
	import org.mxhero.console.configurations.application.event.RemoveEmailAccountEvent;
	import org.mxhero.console.configurations.application.resources.AccountsProperties;
	import org.mxhero.console.configurations.presentation.ConfigurationsViewPM;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.EmailAccount;
	import org.mxhero.console.frontend.domain.Page;

	[Landmark(name="main.dashboard.configurations.accounts")]
	public class AccountsViewPM
	{
		[Bindable]
		private var accountShow:AccountShow;
		
		[Bindable]
		private var rm:IResourceManager = ResourceManager.getInstance();
		
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
		public var selectedEmailAccount:Object;
		
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
			_emailFilter=null;
			_nameFilter=null;
			_lastNameFilter=null;
			findAccounts(0);
		}
		
		public function nextPage():void{
			findAccounts(actualPage.number+1);
		}
		
		public function prevPage():void{
			findAccounts(actualPage.number-1);
		}
		
		public function filterEmailAccounts(email:String,name:String,lastName:String):void{
			_emailFilter=email;
			_nameFilter=name;
			_lastNameFilter=lastName;
			findAccounts(0);
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
			accountShow=new AccountShow();
			accountShow.account=new EmailAccount();
			accountShow.model=this;
			accountShow.currentState="new";
			PopUpManager.addPopUp(accountShow,parent,true);
			PopUpManager.centerPopUp(accountShow);
			PopUpManager.centerPopUp(accountShow);
		}

		public function insertAccount(account:EmailAccount):void{
			dispatcher(new InsertEmailAccountEvent(context.selectedDomain.id,account));
			isLoading=true;
		}
		
		[CommandResult]
		public function insertResult (result:*, event:InsertEmailAccountEvent) : void {
			isLoading=false;
			loadEmailAccounts();
			PopUpManager.removePopUp(accountShow);
		}
		
		[CommandError]
		public function insertError (faultEvent:FaultEvent, event:InsertEmailAccountEvent) : void {
			isLoading=false;
			accountShow.errorText.showError(ErrorTranslator.translate(faultEvent.fault.faultCode));
		}
		
		public function removeEmailAccount():void{
			Alert.show(rm.getString(AccountsProperties.NAME,AccountsProperties.REMOVE_EMAIL_ACCOUNT_CONFIRMATION_TEXT),selectedEmailAccount.email,Alert.YES|Alert.NO,null,removeHandler);
		}
		
		[CommandResult]
		public function removeResult (result:*, event:RemoveEmailAccountEvent) : void {
			var itemPosition:Number= actualPage.elements.getItemIndex(selectedEmailAccount);
			if(itemPosition>-1){
				actualPage.elements.removeItemAt(itemPosition);
			}
			isLoading=false;
		}
		
		public function removeHandler(event:CloseEvent):void{
			if(event.detail==Alert.YES){
				dispatcher(new RemoveEmailAccountEvent(selectedEmailAccount.id));
			}
		}
		
		public function editEmailAccount(parent:DisplayObject):void{
			accountShow = new AccountShow();
			accountShow.account=(selectedEmailAccount as EmailAccount).clone();
			accountShow.model=this;
			accountShow.currentState="edit";
			PopUpManager.addPopUp(accountShow,parent,true);
			PopUpManager.centerPopUp(accountShow);
			PopUpManager.centerPopUp(accountShow);
		}	
		
		public function updateDomain(account:EmailAccount):void{
			dispatcher(new EditEmailAccountEvent(account));
			isLoading=true;
		}
		
		[CommandResult]
		public function updateResult (result:*, event:EditEmailAccountEvent) : void {
			isLoading=false;
			loadEmailAccounts();
			PopUpManager.removePopUp(accountShow);
			accountShow.cancelBtt_clickHandler(null);
		}

		private function findAccounts(page:Number):void{
			dispatcher(new LoadAllEmailAccountsEvent(context.selectedDomain.id,_emailFilter,_nameFilter,_lastNameFilter,page,pageSize));
			isLoading=true;
		}
	}
}