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
package org.mxhero.console.frontend.application.task
{
	import flash.net.registerClassAlias;
	
	import mx.collections.ArrayCollection;
	import mx.messaging.messages.RemotingMessage;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.commons.resources.CommonsProperties;
	import org.mxhero.console.frontend.application.event.LoadInitialDataEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.application.message.LanguageChangedMessage;
	import org.mxhero.console.frontend.application.message.LoadingMessage;
	import org.mxhero.console.frontend.application.resources.LoadingProperties;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.ApplicationUser;
	import org.spicefactory.lib.task.Task;

	public class LoadUserDataTask extends Task
	{
		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Bindable]
		private var rm:IResourceManager=ResourceManager.getInstance();
		
		[Inject(id="applicationUserService")]
		[Bindable]
		public var service:RemoteObject;
		
		[Inject]
		[Bindable]
		public var applicationContext:ApplicationContext;
		
		public function LoadUserDataTask()
		{
			super();
		}
		
		protected override function doStart ():void{
			dispatcher(new LoadingMessage(rm.getString(LoadingProperties.NAME,LoadingProperties.LOADING_USER_LABEL)));
			service.addEventListener(ResultEvent.RESULT,onResult);
			service.addEventListener(FaultEvent.FAULT,onFault);
			service.getUser();
		}
		
		public function onResult(result:ResultEvent):void{
			if((result.token.message as RemotingMessage).operation=="getUser"){
				applicationContext.applicationUser = result.result as ApplicationUser;
				applicationContext.selectedDomain=applicationContext.applicationUser.domain;
				service.removeEventListener(ResultEvent.RESULT,onResult);
				dispatcher(new LanguageChangedMessage(applicationContext.applicationUser.locale));
				applicationContext.locales=new ArrayCollection(rm.getString(CommonsProperties.NAME,CommonsProperties.LOCALES).split(";"));
				complete();
			}
		}
		
		public function onFault(fault:FaultEvent):void{
			service.removeEventListener(FaultEvent.FAULT,onFault);
			error(fault.fault.faultCode);
			dispatcher(new ApplicationErrorMessage(fault.fault.faultCode));
		}
		
	}
}