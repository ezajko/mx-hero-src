package org.mxhero.console.frontend.application.message
{
	public class LanguageChangedMessage
	{
		private var _locale:String;
		
		public function LanguageChangedMessage(locale:String)
		{
			_locale=locale;
		}
		
		public function get locale():String
		{
			return _locale;
		}

		public function set locale(value:String):void
		{
			_locale = value;
		}

	}
}