package org.mxhero.console.frontend.infrastructure
{
	import mx.modules.ModuleLoader;

	[Bindable]
	public class ModuleData
	{
		
		private var _name:String;
		
		private var _path:String;
		
		private var _moduleLoader:ModuleLoader;
		
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

		public function set moduleLoader(value:ModuleLoader):void
		{
			_moduleLoader = value;
		}

	}
}