package org.mxhero.console.frontend.presentation
{
	import org.mxhero.console.frontend.application.event.LoadInitialDataEvent;
	import org.mxhero.console.frontend.application.event.LoginEvent;
	import org.mxhero.console.frontend.application.message.LoadingMessage;

	[Landmark(name="main.loading")]
	public class LoadingPM
	{
		private var _username:String;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Bindable]
		public var currentLabel:String;	
		
		[Enter(time="every")]
		public function enter():void
		{
			currentLabel="Loading";
		}
		
		[CommandResult]
		public function loginResult (result:*, event:LoginEvent) : void {
			_username=event.username;
			dispatcher(new LoadInitialDataEvent(_username));
		}
		
		[MessageHandler]
		public function handleLogin (message:LoadingMessage) : void {
			currentLabel=message.label;
		}
	}
}