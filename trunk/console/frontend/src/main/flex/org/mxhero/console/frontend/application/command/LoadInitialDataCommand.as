package org.mxhero.console.frontend.application.command
{
	import mx.collections.ArrayCollection;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.frontend.application.event.LoadInitialDataEvent;
	import org.mxhero.console.frontend.application.task.LoadUserDataTask;
	import org.spicefactory.lib.task.SequentialTaskGroup;
	import org.spicefactory.lib.task.Task;
	import org.spicefactory.lib.task.TaskGroup;

	public class LoadInitialDataCommand
	{
		
		[Inject]
		[Bindable(id="initialTasks")]
		public var tasks:Array;
		
		private var _taskGroup:TaskGroup;
		
		public function execute(event:LoadInitialDataEvent):Task{
			_taskGroup = new SequentialTaskGroup("Ïnitial Loading");
			for each(var task:Object in tasks){
				(task as Task).data=event;
				_taskGroup.addTask(task as Task);
			}
			return _taskGroup as Task;
		}
		
		public function result():void{
			_taskGroup.removeAllTasks();
		}
		
		public function error():void{
			_taskGroup.removeAllTasks();
		}
	}
}