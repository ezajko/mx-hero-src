package org.mxhero.console.configurations.presentation.domains
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
	import org.mxhero.console.configurations.application.event.EditDomainEvent;
	import org.mxhero.console.configurations.application.event.InsertDomainEvent;
	import org.mxhero.console.configurations.application.event.LoadAllDomainsEvent;
	import org.mxhero.console.configurations.application.event.RemoveDomainEvent;
	import org.mxhero.console.configurations.application.resources.DomainsShowProperties;
	import org.mxhero.console.configurations.presentation.ConfigurationsViewPM;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.Domain;

	[Landmark(name="main.dashboard.configurations.domains")]
	public class DomainsViewPM
	{
		
		private var domainShow:DomainShow;
				
		[Bindable]
		private var rm:IResourceManager = ResourceManager.getInstance();
		
		[Bindable]
		public var selectDomain:Object;
		
		[Inject]
		[Bindable]
		public var parentModel:ConfigurationsViewPM;
			
		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Inject]
		[Bindable]
		public var context:ApplicationContext;
		
		[Bindable]
		public var domains:ArrayCollection;
			
		[Bindable]
		public var isLoading:Boolean=false;
		
		private var _domainFilter:String=null;
		private var _emailFilter:String=null;
		private var _serverFilter:String=null;
		
		[Enter(time="every")]
		public function every():void{
			loadDomains();
		}
		
		public function loadDomains():void{
			dispatcher(new LoadAllDomainsEvent());
			isLoading=true;
		}
		
		[CommandResult]
		public function loadingResult (result:*, event:LoadAllDomainsEvent) : void {
			if (result is Domain){
				domains=new ArrayCollection();
				domains.addItem(result);
			} else {
				domains=result;	
			}
			if(domains!=null){
				var sortByDomain:Sort=new Sort();
				sortByDomain.fields=[new SortField("domain")];
				domains.sort=sortByDomain;
			}
			isLoading=false;
		}
		
		[CommandError]
		public function loadingError (fault:*, event:LoadAllDomainsEvent) : void {
			isLoading=false;
		}
		
		public function filterDomains(domain:String,email:String,server:String):void{
			_domainFilter=trim(domain).toLowerCase();
			_emailFilter=trim(email).toLowerCase();
			_serverFilter=trim(server).toLowerCase();
			domains.filterFunction=orderFuntion;
			domains.refresh()
		}
		
		public function orderFuntion(object:Object):Boolean{
			if(_domainFilter!=null && _domainFilter.length>0){
				if(object.domain.toLowerCase().search(_domainFilter)==-1){
					return false;
				}
			}
			if(_emailFilter!=null && _emailFilter.length>0){
				if(object.owner==null){
					return false;
				}
				if(object.owner.email.toLowerCase().search(_emailFilter)==-1){
					return false;
				}
			}
			if(_serverFilter!=null && _serverFilter.length>0){
				if(object.server.toLowerCase().search(_serverFilter)==-1){
					return false;
				}
			}
			return true;
		}
		
		private function trim(s:String):String { 
			return s ? s.replace(/^\s+|\s+$/gs, '') : ""; 
		}
		
		public function goBack():void{
			parentModel.navigateTo(ConfigurationsDestinations.LIST);
		}
		
		public function removeDomain():void{
			Alert.show(rm.getString(DomainsShowProperties.NAME,DomainsShowProperties.REMOVE_DOMAIN_CONFIRMATION_TEXT),selectDomain.domain,Alert.YES|Alert.NO,null,removeHandler);
		}
		
		public function removeHandler(event:CloseEvent):void{
			if(event.detail==Alert.YES){
				dispatcher(new RemoveDomainEvent(selectDomain.id));
			}
		}
		
		[CommandResult]
		public function removeResult (result:*, event:RemoveDomainEvent) : void {
			var itemPosition:Number= domains.getItemIndex(selectDomain);
			if(itemPosition>-1){
				domains.removeItemAt(itemPosition);
			}
		}
		
		public function newDomain(parent:DisplayObject):void{
			domainShow = new DomainShow();
			domainShow.domain=new Domain();
			domainShow.model=this;
			domainShow.currentState="new";
			PopUpManager.addPopUp(domainShow,parent,true);
			PopUpManager.centerPopUp(domainShow);
			PopUpManager.bringToFront(domainShow);
		}
		
		public function insertDomain(domain:Domain,hasAdmin:Boolean=false,password:String=null,email:String=null):void{
			dispatcher(new InsertDomainEvent(domain,hasAdmin,password,email));
			isLoading=true;
		}
		
		public function editDomain(parent:DisplayObject):void{
			domainShow = new DomainShow();
			domainShow.domain=(selectDomain as Domain).clone();
			domainShow.model=this;
			domainShow.currentState="edit";
			PopUpManager.addPopUp(domainShow,parent,true);
			PopUpManager.centerPopUp(domainShow);
			PopUpManager.centerPopUp(domainShow);
		}	
		
		public function updateDomain(domain:Domain,hasAdmin:Boolean=false,password:String=null,email:String=null):void{
			dispatcher(new EditDomainEvent(domain,hasAdmin,password,email));
			isLoading=true;
		}
		
		[CommandResult]
		public function insertResult (result:*, event:InsertDomainEvent) : void {
			isLoading=false;
			loadDomains();
			PopUpManager.removePopUp(domainShow);
		}
		
		[CommandError]
		public function insertError (faultEvent:FaultEvent, event:InsertDomainEvent) : void {
			isLoading=false;
			domainShow.errorText.showError(ErrorTranslator.translate(faultEvent.fault.faultCode));
		}
		
		[CommandResult]
		public function updateResult (result:*, event:EditDomainEvent) : void {
			isLoading=false;
			loadDomains();
			PopUpManager.removePopUp(domainShow);
			domainShow.cancelBtt_clickHandler(null);
		}
		
		[CommandError]
		public function updateError (faultEvent:FaultEvent, event:EditDomainEvent) : void {
			isLoading=false;
			domainShow.errorText.showError(ErrorTranslator.translate(faultEvent.fault.faultCode));
		}
		
		public function enterDomain():void{
			context.selectedDomain=selectDomain as Domain;
		}
	}
}