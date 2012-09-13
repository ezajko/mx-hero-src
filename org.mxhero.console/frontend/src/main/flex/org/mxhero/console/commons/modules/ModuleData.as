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
package org.mxhero.console.commons.modules
{
	import spark.modules.ModuleLoader;

	[Bindable]
	public class ModuleData
	{
		
		private var _name:String;
		
		private var _ico:Class;
		
		private var _path:String;
		
		private var _moduleLoader:ModuleLoader;
		
		public function ModuleData(){
			_moduleLoader=new ModuleLoader();
			_moduleLoader.percentHeight=100;
			_moduleLoader.percentWidth=100;
		}

		public function get ico():Class
		{
			return _ico;
		}

		public function set ico(value:Class):void
		{
			_ico = value;
		}

		public function get name():String
		{
			return _name;
		}
		
		public function set name(value:String):void
		{
			_name = value;
		}
		public function get path():String
		{
			return _path;
		}

		public function set path(value:String):void
		{
			_path = value;
		}

		public function get moduleLoader():ModuleLoader
		{
			return _moduleLoader;
		}

	}
}