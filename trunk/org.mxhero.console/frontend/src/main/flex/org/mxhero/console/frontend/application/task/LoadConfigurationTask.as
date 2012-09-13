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
	import org.mxhero.console.frontend.domain.Configuration;
	import org.spicefactory.lib.task.Task;
	
	public class LoadConfigurationTask extends Task
	{
		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Bindable]
		private var rm:IResourceManager=ResourceManager.getInstance();
		
		[Inject(id="loadConfigurationService")]
		[Bindable]
		public var service:RemoteObject;
		
		[Inject]
		[Bindable]
		public var applicationContext:ApplicationContext;
		
		public function LoadConfigurationTask()
		{
			super();
		}
		
		protected override function doStart ():void{
			service.addEventListener(ResultEvent.RESULT,onResult);
			service.addEventListener(FaultEvent.FAULT,onFault);
			service.find();
		}
		
		public function onResult(result:ResultEvent):void{
			applicationContext.configuration=result.result as Configuration;
			service.removeEventListener(ResultEvent.RESULT,onResult);
			complete();
		}
		
		public function onFault(fault:FaultEvent):void{
			service.removeEventListener(FaultEvent.FAULT,onFault);
			error(fault.fault.faultCode);
			dispatcher(new ApplicationErrorMessage(fault.fault.faultCode));
		}
		
	}

}