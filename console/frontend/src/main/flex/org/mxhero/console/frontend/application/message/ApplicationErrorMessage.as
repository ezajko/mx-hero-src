package org.mxhero.console.frontend.application.message
{
	public class ApplicationErrorMessage
	{
		private var _key:String;
		
		private var _data:*;
		
		public var isTextual:Boolean=false;
		
		public function ApplicationErrorMessage(key:String,isTextual:Boolean=false)
		{
			this._key = key;
			this.isTextual=isTextual;
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