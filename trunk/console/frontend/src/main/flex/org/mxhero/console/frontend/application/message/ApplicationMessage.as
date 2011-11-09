package org.mxhero.console.frontend.application.message
{
	public class ApplicationMessage
	{
		private var _key:String;
		
		private var _source:*;
		
		private var _fullMessage:String;
		
		public function ApplicationMessage(source:String, key:String, fullMessage:String=null)
		{
			this._key = key;
			this._source=source;
			this._fullMessage=fullMessage;
		}
		
		public function get key():String
		{
			return _key;
		}

		public function get source():*
		{
			return _source;
		}

		public function set source(value:*):void
		{
			_source = value;
		}

		public function get fullMessage():String
		{
			return _fullMessage;
		}

		public function set fullMessage(value:String):void
		{
			_fullMessage = value;
		}

	}
}