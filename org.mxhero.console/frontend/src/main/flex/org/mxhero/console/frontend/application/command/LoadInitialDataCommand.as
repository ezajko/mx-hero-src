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