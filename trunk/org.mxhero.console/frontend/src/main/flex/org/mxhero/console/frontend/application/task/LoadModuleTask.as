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
	import mx.events.ModuleEvent;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	import mx.rpc.events.FaultEvent;
	
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.application.message.LoadingMessage;
	import org.mxhero.console.commons.resources.ErrorsProperties;
	import org.mxhero.console.frontend.application.resources.LoadingProperties;
	import org.mxhero.console.commons.modules.ModuleData;
	import org.spicefactory.lib.task.Task;

	public class LoadModuleTask extends Task
	{
		[MessageDispatcher]
		public var dispatcher:Function;
			
		[Bindable]
		private var rm:IResourceManager=ResourceManager.getInstance();
		
		private var _moduleData:ModuleData;
		
		public function LoadModuleTask()
		{
			super();
		}
		
		protected override function doStart ():void{
			if(moduleData!=null){
				dispatcher(new LoadingMessage(rm.getString(LoadingProperties.NAME,LoadingProperties.LOADING_MODULE_LABEL)+moduleData.name));
				if(moduleData.moduleLoader!= null){
					moduleData.moduleLoader.addEventListener(ModuleEvent.READY,onReady);
					moduleData.moduleLoader.addEventListener(ModuleEvent.ERROR,onError);
					moduleData.moduleLoader.loadModule(moduleData.path);
					
				} else {
					error(ErrorsProperties.LOADING_MODULE_LOADER_EMPTY);
				}
			} else {
				error(ErrorsProperties.LOADING_MODULE_EMPTY);
			}
		}

		protected function onReady(event:ModuleEvent):void{
			moduleData.moduleLoader.removeEventListener(ModuleEvent.READY,onReady);
			complete();
		}

		protected function onError(event:ModuleEvent):void{
			moduleData.moduleLoader.removeEventListener(ModuleEvent.ERROR,onError);
			error(ErrorsProperties.LOADING_MODULE_ERROR);
			dispatcher(new ApplicationErrorMessage(ErrorsProperties.LOADING_MODULE_ERROR));
		}
		
		public function get moduleData():ModuleData
		{
			return _moduleData;
		}

		public function set moduleData(value:ModuleData):void
		{
			_moduleData = value;
		}

	}
}