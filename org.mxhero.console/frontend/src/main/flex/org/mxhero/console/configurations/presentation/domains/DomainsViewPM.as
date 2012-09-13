/*
* mxHero is a platform that intends to provide a single point of development 
* and single point of distribution for email solutions and enhancements. It does this
* by providing an extensible framework for rapid development and deployment of
* email solutions.
* 
* Copyright (C) 2012  mxHero Inc.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
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
	import org.mxhero.console.commons.infrastructure.parser.StringUtils;
	import org.mxhero.console.configurations.application.ConfigurationsDestinations;
	import org.mxhero.console.configurations.application.event.EditDomainEvent;
	import org.mxhero.console.configurations.application.event.InsertDomainEvent;
	import org.mxhero.console.configurations.application.event.LoadAllDomainsEvent;
	import org.mxhero.console.configurations.application.event.RemoveDomainEvent;
	import org.mxhero.console.configurations.application.resources.DomainsShowProperties;
	import org.mxhero.console.configurations.presentation.ConfigurationsViewPM;
	import org.mxhero.console.features.application.FeaturesDestinations;
	import org.mxhero.console.frontend.application.MainDestination;
	import org.mxhero.console.frontend.application.message.ApplicationMessage;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.Domain;
	import org.mxhero.console.frontend.domain.Page;
	import org.mxhero.console.reports.application.ReportsDestinations;

	[Landmark(name="main.dashboard.configurations.domains")]
	public class DomainsViewPM
	{
		
		private var domainShow:DomainShow;
				
		private var domainFilter:String="";
		
		public static const PAGE_SIZE:Number=10;
		
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
		public var domains:Page;
			
		[Bindable]
		public var isLoading:Boolean=false;

		
		[Enter(time="every")]
		public function every():void{
			loadDomains();
		}
		
		public function loadDomains():void{
			filterDomains("",1);
			isLoading=true;
		}
		
		[CommandResult]
		public function loadingResult (result:*, event:LoadAllDomainsEvent) : void {
			domains=result;	
			isLoading=false;
		}
		
		[CommandError]
		public function loadingError (fault:*, event:LoadAllDomainsEvent) : void {
			isLoading=false;
		}
		
		public function filterDomains(domain:String,page:Number):void{
			domainFilter=domain;
			dispatcher(new LoadAllDomainsEvent(trim(domainFilter).toLowerCase(),page,PAGE_SIZE));
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
				dispatcher(new RemoveDomainEvent(selectDomain.domain));
			}
		}
		
		[CommandResult]
		public function removeResult (result:*, event:RemoveDomainEvent) : void {
			dispatcher(new LoadAllDomainsEvent(trim(domainFilter).toLowerCase(),domains.actualPage,PAGE_SIZE));
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
			domain.domain=StringUtils.trim(domain.domain).toLowerCase();
			if(email!=null){
				email=StringUtils.trim(email).toLowerCase();
			}
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
			if(email!=null){
				email=StringUtils.trim(email).toLowerCase();
			}
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
			parentModel.navigateTo(ConfigurationsDestinations.LIST);
			parentModel.navigateTo(ReportsDestinations.LIST);
			parentModel.navigateTo(FeaturesDestinations.LIST);
			parentModel.navigateTo(MainDestination.HOME);
			var message:String=rm.getString(DomainsShowProperties.NAME,DomainsShowProperties.ENTER_DOMAIN_MESSAGE)+"("+selectDomain.domain+")";
			dispatcher(new ApplicationMessage(null,null,message));
		}
	}
}