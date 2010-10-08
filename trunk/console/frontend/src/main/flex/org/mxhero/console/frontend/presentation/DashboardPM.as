package org.mxhero.console.frontend.presentation
{
	import mx.controls.Alert;
	
	import org.mxhero.console.frontend.application.event.LogoutEvent;
	import org.spicefactory.parsley.core.messaging.MessageProcessor;

	[Landmark(name="main.dashboard")]
	public class DashboardPM
	{
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function logout():void{
			dispatcher(new LogoutEvent());
		}
	}

}