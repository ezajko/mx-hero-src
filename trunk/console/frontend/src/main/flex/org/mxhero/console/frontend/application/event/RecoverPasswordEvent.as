package org.mxhero.console.frontend.application.event
{
	public class RecoverPasswordEvent
	{
		private var _mail:String;
		
		public function RecoverPasswordEvent(mail:String)
		{
		}
		
		public function get mail():String{
			return _mail;
		}
	}
}