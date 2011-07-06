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
	import org.mxhero.console.commons.infrastructure.parser.StringUtils;
	import org.mxhero.console.commons.resources.ErrorsProperties;
	import org.mxhero.console.configurations.application.ConfigurationsDestinations;
	import org.mxhero.console.configurations.application.event.EditEmailAccountEvent;
	import org.mxhero.console.configurations.application.event.InsertEmailAccountEvent;
	import org.mxhero.console.configurations.application.event.LoadAllEmailAccountsEvent;
	import org.mxhero.console.configurations.application.event.LoadAllGroupsEvent;
	import org.mxhero.console.configurations.application.event.RemoveEmailAccountEvent;
	import org.mxhero.console.configurations.application.event.UploadAccountsEvent;
	import org.mxhero.console.configurations.application.resources.AccountsProperties;
	import org.mxhero.console.configurations.presentation.ConfigurationsViewPM;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.EmailAccount;
	import org.mxhero.console.frontend.domain.EmailAccountAlias;

	[Landmark(name="main.dashboard.configurations.accounts")]
	public class AccountsViewPM
	{
		[Bindable]
		private var accountShow:AccountShow;
		
		[Bindable]
		private var accountUpload:AccountUpload;
		
		[Bindable]
		private var rm:IResourceManager = ResourceManager.getInstance();
		
		[Inject]
		[Bindable]
		public var context:ApplicationContext;
		[Bindable]
		public var accounts:ArrayCollection;
		
		[Inject]
		[Bindable]
		public var parentModel:ConfigurationsViewPM;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Bindable]
		public var isLoading:Boolean=false;
		
		[Bindable]
		public var selectedEmailAccount:Object;
		
		private var _accountFilter:String=null;
		private var _groupIdFilter:String=null;
		
		public function goBack():void{
			parentModel.navigateTo(ConfigurationsDestinations.LIST);
		}
		
		[Enter(time="every")]
		public function every():void{
			loadEmailAccounts();
			dispatcher(new LoadAllGroupsEvent(context.selectedDomain.domain));
		}
		
		public function loadEmailAccounts():void{
			_accountFilter=null;
			_groupIdFilter=null;
			findAccounts();
		}
		
		public function filterEmailAccounts(account:String,groupName:String):void{
			_accountFilter=account;
			_groupIdFilter=groupName;
			findAccounts();
		}
		
		[CommandResult]
		public function loadingResult (result:*, event:LoadAllEmailAccountsEvent) : void {
			accounts=result;
			isLoading=false;
		}
		
		[CommandError]
		public function loadingError (fault:*, event:LoadAllEmailAccountsEvent) : void {
			isLoading=false;
		}
		
		public function newEmailAccount(parent:DisplayObject):void{
			accountShow=new AccountShow();
			accountShow.account=new EmailAccount();
			accountShow.account.domain=context.selectedDomain.domain;
			accountShow.model=this;
			accountShow.currentState="new";
			PopUpManager.addPopUp(accountShow,parent,true);
			PopUpManager.centerPopUp(accountShow);
			PopUpManager.centerPopUp(accountShow);
		}

		public function insertAccount(account:EmailAccount):void{
			account.account=StringUtils.trim(account.account).toLowerCase();
			dispatcher(new InsertEmailAccountEvent(context.selectedDomain.domain,account));
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
			Alert.show(rm.getString(AccountsProperties.NAME,AccountsProperties.REMOVE_EMAIL_ACCOUNT_CONFIRMATION_TEXT),selectedEmailAccount.account,Alert.YES|Alert.NO,null,removeHandler);
		}
		
		[CommandResult]
		public function removeResult (result:*, event:RemoveEmailAccountEvent) : void {
			var itemPosition:Number= accounts.getItemIndex(selectedEmailAccount);
			if(itemPosition>-1){
				accounts.removeItemAt(itemPosition);
			}
			isLoading=false;
		}
		
		public function removeHandler(event:CloseEvent):void{
			if(event.detail==Alert.YES){
				dispatcher(new RemoveEmailAccountEvent(selectedEmailAccount.account,selectedEmailAccount.domain));
			}
		}
		
		public function editEmailAccount(parent:DisplayObject):void{
			accountShow = new AccountShow();
			accountShow.account=(selectedEmailAccount as EmailAccount).clone();
			accountShow.model=this;
			accountShow.currentState="edit";
			accountShow.domains=new ArrayCollection();
			if(context.selectedDomain.aliases!=null){
				accountShow.domains.addAll(context.selectedDomain.aliases);
			}
			PopUpManager.addPopUp(accountShow,parent,true);
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

		private function findAccounts():void{
			dispatcher(new LoadAllEmailAccountsEvent(context.selectedDomain.domain,_accountFilter,_groupIdFilter));
			isLoading=true;
		}
		
		public function uploadAccount(parent:DisplayObject):void{
			accountUpload=new AccountUpload();
			accountUpload.model=this;
			PopUpManager.addPopUp(accountUpload,parent,true);
			PopUpManager.centerPopUp(accountUpload);
			PopUpManager.bringToFront(accountUpload)
		}
		
		public function uploadAllAccounts(uploadAccounts:ArrayCollection,failOnError:Boolean):void{
			dispatcher(new UploadAccountsEvent(uploadAccounts,context.selectedDomain.domain,failOnError));
			isLoading=true;
		}
		
		[CommandResult]
		public function uploadResult (result:*, event:UploadAccountsEvent) : void {
			isLoading=false;
			var emailAccountsDuplicated:ArrayCollection=null;
			if(result is EmailAccount){
				emailAccountsDuplicated=new ArrayCollection();
				emailAccountsDuplicated.addItem(emailAccountsDuplicated);
			} else if(result is ArrayCollection && (result as ArrayCollection).length>0){
				emailAccountsDuplicated=result as ArrayCollection;
			}
			if(emailAccountsDuplicated!=null){
				accountUpload.errorText.showError(rm.getString(ErrorsProperties.NAME,ErrorsProperties.EMAIL_UPLOAD_CHECKED_ERRORS));
				accountUpload.uploadedAccounts=emailAccountsDuplicated;
			}else{
				accountUpload.cancelBtt_clickHandler();
			}
			loadEmailAccounts();
		}
		
		[CommandError]
		public function uploadError (faultEvent:FaultEvent, event:UploadAccountsEvent) : void {
			isLoading=false;
			accountUpload.errorText.showError(ErrorTranslator.translate(faultEvent.fault.faultCode));
		}
	}
}