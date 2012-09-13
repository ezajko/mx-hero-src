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
package org.mxhero.console.frontend.presentation
{
	import com.adobe.cairngorm.navigation.NavigationEvent;
	import com.adobe.cairngorm.navigation.state.ISelectedName;
	
	import flash.system.Security;
	
	import mx.collections.ArrayCollection;
	import mx.containers.ViewStack;
	import mx.controls.Alert;
	import mx.core.Container;
	import mx.core.FlexGlobals;
	import mx.effects.SoundEffect;
	import mx.events.CloseEvent;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	
	import org.mxhero.console.configurations.application.ConfigurationsDestinations;
	import org.mxhero.console.features.application.FeaturesDestinations;
	import org.mxhero.console.frontend.application.MainDestination;
	import org.mxhero.console.frontend.application.event.LogoutEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.application.message.ApplicationMessage;
	import org.mxhero.console.frontend.application.message.LanguageChangedMessage;
	import org.mxhero.console.frontend.application.resources.DashboardProperties;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.Domain;
	import org.mxhero.console.frontend.infrastructure.AuthorizeHelper;
	import org.mxhero.console.reports.application.ReportsDestinations;
	import org.spicefactory.parsley.core.messaging.MessageProcessor;

	[Landmark(name="main.dashboard")]
	public class DashboardPM
	{

		[Bindable]
		public var menuDataProvider:ArrayCollection;
		
		[Bindable]
		private var rm:IResourceManager = ResourceManager.getInstance();

		[Bindable]
		public static var container:Container;
		
		[Inject]
		[Bindable]
		public var context:ApplicationContext;
		
		[MessageDispatcher]
		public var dispatcher:Function;

		[Inject]
		[Bindable]			
		public var authorizeHelper:AuthorizeHelper;
		
		[Bindable]
		[CommandStatus(type="org.mxhero.console.frontend.application.event.LogoutEvent")]
		public var isLogingout:Boolean = false;
		
		[Enter(time="first")]
		public function enter():void
		{
			container.createComponentsFromDescriptors();
			dispatcher(NavigationEvent.createNavigateToEvent(MainDestination.HOME));
			var url:String = FlexGlobals.topLevelApplication.url as String;
			url = url.substr(0,url.lastIndexOf("/"));
			try{
				Security.loadPolicyFile(url+"/crossdomain.xml");
			}catch( err:Error){
				dispatcher(new ApplicationErrorMessage(err.message,true));
			}
		}
		
		public function logout():void{
			if(context.selectedDomain!=null 
				&& authorizeHelper.checkUserForAuthority('ROLE_ADMIN')){
				Alert.show(rm.getString(DashboardProperties.NAME,DashboardProperties.ALERT_LOGOUT_DOMAIN_CONFIRM_TEXT),context.selectedDomain.domain,Alert.YES|Alert.NO,null,leaveDomainHandler);
			}else{
				Alert.show(rm.getString(DashboardProperties.NAME,DashboardProperties.ALERT_LOGOUT_CONFIRM_TEXT),"",Alert.YES|Alert.NO,null,logoutHandler);
			}
		}
		
		public function logoutHandler(event:CloseEvent):void{
			if(event.detail==Alert.YES){
				dispatcher(new LogoutEvent());
			}
		}

		public function leaveDomainHandler(event:CloseEvent):void{
			if(event.detail==Alert.YES){
				var message:String=rm.getString(DashboardProperties.NAME,DashboardProperties.DOMAIN_LEAVE_MESSAGE)+"("+context.selectedDomain.domain+")";
				dispatcher(new ApplicationMessage(null,null,message));
				context.selectedDomain=null;
				dispatcher(NavigationEvent.createNavigateToEvent(ConfigurationsDestinations.LIST));
				dispatcher(NavigationEvent.createNavigateToEvent(ReportsDestinations.LIST));
				dispatcher(NavigationEvent.createNavigateToEvent(FeaturesDestinations.LIST));
				dispatcher(NavigationEvent.createNavigateToEvent(MainDestination.HOME));
			}
		}
		
		public function hasToShowDomain(domain:Domain):Boolean{
			if(domain!=null && authorizeHelper.checkUserForAuthority('ROLE_ADMIN')){
				return true;
			}
			return false;
		}
		
	}

}