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

	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	
	import org.mxhero.console.frontend.application.MainDestination;
	import org.mxhero.console.frontend.application.event.LoadInitialDataEvent;
	import org.mxhero.console.frontend.application.event.LoginEvent;
	import org.mxhero.console.frontend.application.event.RecoverPasswordEvent;
	import org.mxhero.console.frontend.application.message.ApplicationMessage;
	import org.mxhero.console.frontend.application.message.ViewChangedMessage;
	import org.mxhero.console.frontend.application.resources.LoginProperties;
	import org.spicefactory.parsley.core.messaging.MessageProcessor;
	
	[Landmark(name="main")]
	public class MainViewPM
	{	
		
		[MessageDispatcher]
		public var dispatcher:Function
		
		[Bindable]
		private var rm:IResourceManager = ResourceManager.getInstance();
		
		public function navigateTo(destination:String):void
		{
			dispatcher(NavigationEvent.createNavigateToEvent(destination));
		}
		
		[CommandResult]
		public function loginResult (result:*, event:LoginEvent) : void {
			dispatcher(new ViewChangedMessage());
			navigateTo(MainDestination.LOADING);
			
		}
		
		[CommandResult]
		public function loadingResult (result:*, event:LoadInitialDataEvent) : void {
			navigateTo(MainDestination.DASHBOARD);
		}
		
		[CommandResult]
		public function recoverResult (result:*, event:RecoverPasswordEvent) : void {
			dispatcher(new ViewChangedMessage());
			dispatcher(new ApplicationMessage(LoginProperties.NAME,LoginProperties.PASSWORD_SENT_TOMAIL));
		}
	}
}