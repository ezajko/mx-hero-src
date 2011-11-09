package org.mxhero.console.frontend.application.message
{
	public class LoadingMessage
	{
		private var _label:String;
		
		public function LoadingMessage(label:String)
		{
			_label=label;
		}
		
		public function get label():String{
			return _label;
		}
	}
}