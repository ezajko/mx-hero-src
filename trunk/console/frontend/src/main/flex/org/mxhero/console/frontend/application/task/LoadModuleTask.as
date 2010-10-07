package org.mxhero.console.frontend.application.task
{
	import mx.events.ModuleEvent;
	import mx.rpc.events.FaultEvent;
	
	import org.mxhero.console.frontend.application.message.LoadingMessage;
	import org.mxhero.console.frontend.infrastructure.ModuleData;
	import org.spicefactory.lib.task.Task;

	public class LoadModuleTask extends Task
	{
		[MessageDispatcher]
		public var dispatcher:Function;
		
		private var _moduleData:ModuleData;
		
		public function LoadModuleTask()
		{
			super();
		}
		
		protected override function doStart ():void{
			if(moduleData!=null){
				dispatcher(new LoadingMessage("Loading Module "+moduleData.name));
				if(moduleData.moduleLoader!= null){
					moduleData.moduleLoader.addEventListener(ModuleEvent.READY,onReady);
					moduleData.moduleLoader.addEventListener(ModuleEvent.ERROR,onError);
					moduleData.moduleLoader.loadModule(moduleData.path);
					
				} else {
					error("Module loader is not asigned for "+moduleData.name);
				}
			} else {
				error("Module data is empty");
			}
		}

		protected function onReady(event:ModuleEvent):void{
			moduleData.moduleLoader.removeEventListener(ModuleEvent.READY,onReady);
			complete();
		}

		protected function onError(event:ModuleEvent):void{
			moduleData.moduleLoader.removeEventListener(ModuleEvent.ERROR,onError);
			error("Error while loading module for "+moduleData.name);
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