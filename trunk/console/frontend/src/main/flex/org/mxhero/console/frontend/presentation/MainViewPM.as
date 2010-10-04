package org.mxhero.console.frontend.presentation
{
	import com.adobe.cairngorm.navigation.NavigationEvent;
	
	import org.mxhero.console.frontend.application.MainDestination;
	import org.mxhero.console.frontend.application.event.LoginEvent;
	
	[Landmark(name="main")]
	public class MainViewPM
	{	
		[MessageDispatcher]
		public var dispatcher:Function
		
		private function navigateTo(destination:String):void
		{
			dispatcher(NavigationEvent.createNavigateToEvent(destination));
		}
		
		[CommandResult(type="org.mxhero.console.frontend.application.event.LoginEvent")]
		private function loginResult (trigger:LoginEvent) : void {
			navigateTo(MainDestination.LOADING);
		}
	}
}