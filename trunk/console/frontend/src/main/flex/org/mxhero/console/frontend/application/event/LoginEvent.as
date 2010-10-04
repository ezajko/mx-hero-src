package org.mxhero.console.frontend.application.event
{
	public class LoginEvent
	{
		private var _username:String;
		
		private var _password:String;
		
		public function LoginEvent(username:String, password:String)
		{
			this._username=username;
			this._password=password;
		}
		
		public function get username():String
		{
			return _username;
		}
		
		public function get password():String
		{
			return _password;
		}

	}
}