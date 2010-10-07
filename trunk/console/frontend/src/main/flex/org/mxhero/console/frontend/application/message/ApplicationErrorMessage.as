package org.mxhero.console.frontend.application.message
{
	public class ApplicationErrorMessage
	{
		private var _key:String;
		
		private var _data:*;
		
		public function ApplicationErrorMessage(key:String)
		{
			this._key = key;
		}
		
		public function get key():String
		{
			return _key;
		}

		public function get data():*
		{
			return _data;
		}

		public function set data(value:*):void
		{
			_data = value;
		}

	}
}