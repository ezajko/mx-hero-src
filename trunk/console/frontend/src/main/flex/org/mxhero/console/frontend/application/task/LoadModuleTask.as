package org.mxhero.console.frontend.application.task
{
	import mx.events.ModuleEvent;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	import mx.rpc.events.FaultEvent;
	
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.application.message.LoadingMessage;
	import org.mxhero.console.frontend.infrastructure.ErrorsProperties;
	import org.mxhero.console.frontend.infrastructure.LoadingProperties;
	import org.mxhero.console.frontend.infrastructure.ModuleData;
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