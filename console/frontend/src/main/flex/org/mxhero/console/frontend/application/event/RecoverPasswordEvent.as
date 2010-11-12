package org.mxhero.console.frontend.application.event
{
	public class RecoverPasswordEvent
	{
		private var _mail:String;
		
		public function RecoverPasswordEvent(mail:String)
		{
			this._mail=mail;
		}
		
		public function get mail():String{
			return _mail;
		}
	}
}