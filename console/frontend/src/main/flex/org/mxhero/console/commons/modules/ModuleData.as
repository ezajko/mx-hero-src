package org.mxhero.console.commons.modules
{
	import mx.modules.ModuleLoader;

	[Bindable]
	public class ModuleData
	{
		
		private var _name:String;
		
		private var _ico:Class;
		
		private var _path:String;
		
		private var _moduleLoader:ModuleLoader;
		
		public function ModuleData(){
			_moduleLoader=new ModuleLoader();
			_moduleLoader.creationPolicy="auto";
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