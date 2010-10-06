package org.mxhero.console.frontend.application.event
{
	public class LoadInitialDataEvent
	{
		private var _username:String;
		
		public function LoadInitialDataEvent(username:String)
		{
			_username=username;
		}
		
		public function get username():String{
			return _username;
		}
	}
}