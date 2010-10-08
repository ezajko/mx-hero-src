package org.mxhero.console.frontend.presentation
{
	import org.mxhero.console.frontend.application.event.LoadInitialDataEvent;
	import org.mxhero.console.frontend.application.event.LoginEvent;
	import org.mxhero.console.frontend.application.message.LoadingMessage;

	[Landmark(name="main.loading")]
	public class LoadingPM
	{

		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Bindable]
		public var currentLabel:String;	
		
		[Enter(time="every")]
		public function enter():void
		{
			currentLabel="Loading";
			dispatcher(new LoadInitialDataEvent());
		}
		
		[MessageHandler]
		public function handleLoading (message:LoadingMessage) : void {
			currentLabel=message.label;
		}
	}
}